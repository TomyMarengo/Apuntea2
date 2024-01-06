import type { Metadata } from "next";
import { Lato } from "next/font/google";
import "./globals.css";
import Navbar from "./ui/Navbar/Navbar";

const lato = Lato({ weight: ["400", "700", "900"], subsets: ["latin-ext"] });

export const metadata: Metadata = {
  title: {
    template: "%s | Apuntea",
    default: "Apuntea",
  },
  description: "Apuntea is a note-sharing app for students.",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={lato.className}>
        <Navbar />
        {children}
      </body>
    </html>
  );
}
