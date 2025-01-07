import React from "react";

function ImageUploader({
                           file,
                           setFile,
                           imagePreviewUrl,
                           setImagePreviewUrl
                       }: {
    file: File | null;
    setFile: (file: File) => void;
    imagePreviewUrl: string | null;
    setImagePreviewUrl: (url: string) => void;
}) {

    // Function to handle image file changes
    const handleImageChange = (event) => {
        event.preventDefault();

        const fileDummy = event.target.files[0]; // Get the selected file

        setFile(fileDummy); // Update the file state

        const reader = new FileReader(); // Create a FileReader to read the file
        reader.onloadend = () => {
            if (typeof reader.result === 'string') {
                setImagePreviewUrl(reader.result as string); // Set the image preview URL
            }
        };

        if (fileDummy) {
            reader.readAsDataURL(fileDummy); // Read the file as a data URL
        }
    };

    // Function to handle the "Select Image" button click
    const handleUploadClick = () => {
        const inputElement = document.getElementById('imageInput') as HTMLInputElement; // Get the file input element
        if (inputElement) {
            inputElement.click(); // Trigger the file input click event
        }
    };

    return (
        <div>
            <input
                id="imageInput"
                type="file"
                accept=".jpg, .png"
                onChange={handleImageChange}
                style={{ display: 'none' }} // Hide the file input element
            />

            <button onClick={handleUploadClick}>Select Image</button> {/* Button to trigger file input */}

            {imagePreviewUrl && (
                <img src={imagePreviewUrl} alt="Selected Image" width="200" height="150" />
                )}

            {file && (
                <>
                    <p>Selected Image: {file.name}</p> {/* Display the selected file name */}
                    <p>Size: {(file.size / 1024).toFixed(2)} KB</p> {/* Display the selected file size in KB */}
                </>
            )}
        </div>
    );
}

export default ImageUploader;