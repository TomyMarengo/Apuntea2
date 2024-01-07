"use client";

import LoginForm from "@/ui/LoginForm";
import NoteIcon from "@/ui/NoteIcon/NoteIcon";
import { FileType, Note } from "@/lib/definitions";

export default function Login() {
  const note: Note = {
    fileType: FileType.PDF,
    name: "Holis",
    self: "/",
  };

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24 ">
      <LoginForm />
      <NoteIcon {...note} />
    </main>
  );
}
