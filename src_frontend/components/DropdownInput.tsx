
interface DropdownInputProps {
    selectedValue: string; // The currently selected value
    setSelectedValue: (newValue: string) => void; // Function to update the selected value
    options: string[]; // Array of options for the dropdown
}

function DropdownInput({ selectedValue, setSelectedValue, options }: DropdownInputProps) {
    // Function to handle changes in the dropdown
    const handleChange = (event) => {
        setSelectedValue(event.target.value); // Update the selected value
    };

    return (
        <div>
            <select value={selectedValue} onChange={handleChange}>
                {/* Render the currently selected value as the first option */}
                <option value={selectedValue} key={selectedValue}>
                    {selectedValue}
                </option>
                {/* Map through the options and render each one as an option in the dropdown */}
                {options.map((option) => {
                    if (option !== selectedValue) {
                        return (
                            <option key={option} value={option}>
                                {option}
                            </option>
                        );
                    }
                })}
            </select>
        </div>
    );
}

export default DropdownInput;