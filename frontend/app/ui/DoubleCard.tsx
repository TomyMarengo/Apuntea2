import clsx from "clsx";
import ApunteaCard from "./ApunteaCard";

interface DoubleCardProps {
    title: string;
    className?: string;
    children?: React.ReactNode;
}

const DoubleCard = ({ 
    title = "Login", 
    className, 
    children 
}: DoubleCardProps) => {
    return (
        <div className="card box">
            <div className="row">
                <div className="col-lg-6">
                    <h1>{title}</h1>
                    {children}
                </div>
                <div className="col-lg-6 box d-flex align-items-center we-are-more-container">
                    <ApunteaCard/>
                </div>
            </div>
        </div>
    );
}

export default DoubleCard;