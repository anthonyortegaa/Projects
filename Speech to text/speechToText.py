from vosk import Model, KaldiRecognizer
import pyaudio
import json
import os

# Ensure the model exists and load the model
model_path = r"Speech to text\model"

if not os.path.exists(model_path):
    raise FileNotFoundError(f"Vosk model not found at {model_path}. Check the path.")
model = Model(model_path)

# Set up PyAudio
p = pyaudio.PyAudio()
stream = p.open(
    format=pyaudio.paInt16, # paInt16 : less mem usage | paFloat32 : more mem usage
    channels=1,
    rate=16000, # must match the KaldiRecognizer
    input=True,
    frames_per_buffer=8192, 
)

# Output file
output_file = r"Speech to text\speechToText.txt"

# Ask user if they want to start a new transcription
user_choice = input("Do you want to start a new transcription? (yes/no): ").strip().lower()
if user_choice in ["yes", "y"]:
    with open(output_file, "w", encoding="utf-8") as file:
        file.write("")
    print("Starting a new transcription. Previous data has been erased.")
else:
    print("Continuing from the existing transcription.")

# Start transcribing
rec = KaldiRecognizer(model, 16000)
print("Listening... (Press Ctrl+C to stop)")

with open(output_file, "a", encoding="utf-8") as file:
    try:
        while True:
            try:
                data = stream.read(4096, exception_on_overflow=False)  # Prevents crash on overflow
                if rec.AcceptWaveform(data):
                    result = json.loads(rec.Result())["text"]
                    if result.strip():  # Avoid empty results
                        print(f"Recognized: {result}")
                        file.write(result + "\n")
                        file.flush()  # Save immediately
            except OSError:
                print("Audio buffer overflow! Skipping frame.")
                with open(output_file, "a", encoding="utf-8") as file:
                   file.write("[WARNING: Audio buffer overflow - Some speech may be lost]\n")
                continue  # Skip problematic frames
    except KeyboardInterrupt:
        print("\nStopping transcription.")
        stream.stop_stream()
        stream.close()
        p.terminate()