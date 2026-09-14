

const startButton = document.getElementById("startButton")
const stopButton = document.getElementById("stopButton")
const statusText = document.getElementById("status")
const transcriptionText = document.getElementById("transcription");

let mediaRecorder;
let audioChunks = [];
let audioStream;

startButton.addEventListener("click", async () => {
	

    try {
        audioStream = await navigator.mediaDevices.getUserMedia({
            audio: true
        });

        audioChunks = [];

        mediaRecorder = new MediaRecorder(audioStream);

        mediaRecorder.addEventListener("dataavailable", (event) => {
            if (event.data.size > 0) {
                audioChunks.push(event.data);
            }
        });

        mediaRecorder.addEventListener("stop", async () => {

            const audioBlob = new Blob(audioChunks, {
                type: mediaRecorder.mimeType
            });

            audioStream.getTracks().forEach((track) => {
                track.stop();
            });

            await uploadAudio(audioBlob);

            startButton.disabled = false;
        });

        mediaRecorder.start();

        statusText.textContent = "Recording...";

        startButton.disabled = true;
        stopButton.disabled = false;

    } catch (error) {
        console.error(error);

        statusText.textContent = "Could not access microphone";
    }
});

stopButton.addEventListener("click", () => {
	if (mediaRecorder) {

	mediaRecorder.stop();

	statusText.textContent = "Processing...";

	stopButton.disabled = true;
	}
});

async function uploadAudio(audioBlob) {
	
	const formData = new FormData();
	
	formData.append(
		"file", audioBlob, "recording.webm"
	);
	
	try {
		const response = await fetch("/api/v1/transcriptions", {
			method: "POST",
			body: formData
		});
		
		if (!response.ok) {
			throw new Error("Upload failed");
		}
		
		const message = await response.text();
		
		statusText.textContent = message;
		
		
	} catch (error) {
		console.error(error);
		
		statusText.textContent = "Error uploading recording.";
	}
	
}

