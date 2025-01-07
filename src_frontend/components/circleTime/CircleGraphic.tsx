import styles from "./CircleGraphic.module.css";

// Define the interface for the CircleProps
interface CircleProps {
    radius: number; // The radius of the circle
    value: number; // The value to be displayed and used to calculate the dash offset
    name: string; // The name to be displayed below the circle
}

// Define the CircleGraphic component
const CircleGraphic: React.FC<CircleProps> = ({ radius, value, name }) => {
    // Calculate the circumference of the circle
    const circumference = 2 * Math.PI * radius;

    // Calculate the size of the viewBox
    const viewboxSize = 2 * radius + 4;

    // Function to calculate the dash offset based on the value
    function getDashOffset(offset: number) {
        // The offset is calculated as a fraction of the circumference based on the value
        return (circumference * (60 - offset) / 60);
    }

    return (
        <div className={styles['circle']}>
            <div className={styles['circleGraphic']}>
                <svg
                    viewBox={"0 0 " + viewboxSize.toString() + " " + viewboxSize.toString()}
                    preserveAspectRatio={"xMinYMin meet"}
                >
                    {/* Background circle with grey stroke */}
                    <circle
                        r={radius}
                        stroke={"grey"}
                        strokeWidth={"1"}
                        fill={"transparent"}
                        cx={radius + 2}
                        cy={radius + 2}
                    ></circle>

                    {/* Progress circle with green stroke */}
                    <circle
                        r={radius}
                        stroke={"green"}
                        strokeWidth={"3"}
                        fill={"transparent"}
                        cx={radius + 2}
                        cy={radius + 2}
                        strokeDasharray={circumference.toString()}
                        strokeDashoffset={getDashOffset(value).toString()}
                        transform={
                            "rotate(-90 " +
                            (viewboxSize / 2).toString() +
                            " " +
                            (viewboxSize / 2).toString() +
                            ")"
                        }
                    ></circle>
                </svg>

                {/* Text inside the circle */}
                <div className={styles['circleText']}>
                    <p>
                        <b>{value}</b>
                        Min
                    </p>
                </div>
            </div>

            {/* Name below the circle */}
            <h5>{name}</h5>
        </div>
    );
};

export default CircleGraphic;