import React from "react";

interface NumericInputProps {
    value: string; // The current value of the input
    setValue: (newValue: string) => void; // Function to update the value
}

function NumericInput({ value, setValue }: NumericInputProps) {
    // Function to handle input changes
    const handleInputChange = (event) => {
        const inputValue = event.target.value; // Get the input value from the event

        // Remove any characters that are not digits or a decimal point
        const numericValue = inputValue.replace(/[^0-9.]/g, '');

        // Split the numeric value by the decimal point
        const parts = numericValue.split('.');

        // Ensure there is only one decimal point
        if (parts.length > 2) {
            return;
        }

        // Update the value with the cleaned numeric value
        setValue(numericValue);
    };

    return (
        <input
            type="text"
            value={value}
            onChange={handleInputChange}
        />
    );
}

export default NumericInput;