import styles from "./DirectoryItem.module.css";
import type { Directory } from "@/lib/definitions";
import Link from "next/link";
import { DirectoryIcon } from "../../Icons";

const DirectoryItem = (directory: Directory) => {
  return (
    <Link className={styles.DirectoryItem} href={directory.self}>
      <DirectoryIcon width={40} height={40} />
      <div className="line-clamp-2">{directory.name}</div>
    </Link>
  );
};

export default DirectoryItem;
