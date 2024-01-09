import { DownloadIcon } from "@/ui/Icons";
import clsx from "clsx";

type DownloadIconButtonProps = {
  fileName: string;
  link: string;
  className?: string;
};

const DownloadIconButton = ({
  fileName,
  link,
  className,
}: DownloadIconButtonProps) => {
  return (
    <a href={link} download={fileName}>
      <button className="icon-button">
        <DownloadIcon className={clsx("icon-s", className)} />
      </button>
    </a>
  );
};

export default DownloadIconButton;
