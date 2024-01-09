"use client";

import LoginForm from "@/ui/LoginForm";
import NoteItem from "@/ui/Icons/NoteItem/NoteItem";
import { FileType, Note, Directory } from "@/lib/definitions";
import DirectoryItem from "@/ui/Icons/DirectoryItem/DirectoryItem";
import DownloadIconButton from "@/ui/Icons/DownloadIconButton/DownloadIconButton";
import CopyIconButton from "@/ui/Icons/CopyIconButton/CopyIconButton";
export default function Login() {
  const note: Note = {
    fileType: FileType.PDF,
    name: "Holis",
    self: "/",
  };

  const directory: Directory = {
    name: "Holis",
    self: "/",
  };

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24 ">
      <LoginForm />

      <NoteItem {...note} />
      <DirectoryItem {...directory} />
      <DownloadIconButton
        fileName="hola"
        link="http://localhost:3000/_next/image?url=%2Fimage%2Fpdf.png&w=96&q=75"
        className="fill-dark-primary"
      />
      <CopyIconButton link="hola mundo" className="fill-dark-primary" />
    </main>
  );
}
