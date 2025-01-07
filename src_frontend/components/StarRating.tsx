import React, { useState } from "react";

interface StarRatingProps {
    rating?: number; // The current rating (default is 0)
    onRate?: (rating: number) => void; // Callback function to handle rating changes
}

const StarRating: React.FC<StarRatingProps> = ({ rating = 0, onRate }) => {
    // State to store the current hover rating
    const [hoverRating, setHoverRating] = useState<number | null>(null);

    // Function to handle mouse enter on a star
    const handleMouseEnter = (index: number) => {
        setHoverRating(index + 1); // Set the hover rating to the current star index + 1
    };

    // Function to handle mouse leave from a star
    const handleMouseLeave = () => {
        setHoverRating(null); // Reset the hover rating
    };

    // Function to handle click on a star
    const handleClick = (index: number) => {
        if (onRate) {
            onRate(index + 1); // Call the onRate callback with the current star index + 1
        }
    };

    return (
        <div className="star-rating">
            {/* Map through an array of 10 elements to create 10 stars */}
            {[...Array(10)].map((_, index) => (
                <svg
                    key={index} // Unique key for each star
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill={hoverRating || rating > index ? "gold" : "gray"} // Set the fill color based on the hover rating or the current rating
                    onMouseEnter={() => handleMouseEnter(index)} // Handle mouse enter event
                    onMouseLeave={handleMouseLeave} // Handle mouse leave event
                    onClick={() => handleClick(index)} // Handle click event
                >
                    <path d="M12 .587l3.668 7.568 8.332 1.151-6.064 5.828 1.48 8.279-7.416-3.967-7.417 3.967 1.481-8.279-6.064-5.828 8.332-1.151z" />
                </svg>
            ))}
        </div>
    );
};

export default StarRating;