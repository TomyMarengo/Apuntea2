import { LinkIcon } from "@/ui/Icons";
import clsx from "clsx";

type CopyIconButtonProps = {
  link: string;
  className?: string;
};

const CopyIconButton = ({ link, className }: CopyIconButtonProps) => {
  return (
    <button
      className="icon-button"
      onClick={() => {
        navigator.clipboard.writeText(link);
      }}
    >
      <LinkIcon className={clsx("icon-s", className)} />
    </button>
  );
};

export default CopyIconButton;
