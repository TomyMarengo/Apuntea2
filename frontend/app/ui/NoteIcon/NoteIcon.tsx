import Image from "next/image";
import styles from "./NoteIcon.module.css";
import type { Note } from "@/lib/definitions";
import Link from "next/link";

const NoteIcon = (note: Note) => {
  const fileTypeImage = "/image/" + note.fileType + ".png";
  return (
    <Link className={styles.noteIcon} href={note.self}>
      <Image src={fileTypeImage} alt={note.fileType} width={40} height={40} />
      <div className="line-clamp-2">{note.name}</div>
    </Link>
  );
};

export default NoteIcon;
