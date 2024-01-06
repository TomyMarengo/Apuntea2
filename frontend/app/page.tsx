"use client";
import { useState } from "react";
import Button from "@/ui/Button/Button";
import Input from "@/ui/Input/Input";
import InputPassword from "./ui/Input/InputPassword";
import InputAutocomplete from "./ui/Input/InputAutocomplete";

export default function Home() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = () => {
    // TODO
  };
  const institutions = [
    { label: "ITBA", value: "ITBA" },
    { label: "UBA", value: "UBA" },
  ];

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24 ">
      Apuntea
      <form>
        <Input
          type="email"
          placeholder="Email"
          onChange={(e) => setEmail(e.target.value)}
        />
        <InputPassword onChange={(e) => setPassword(e.target.value)} />
        <Button label="Button" onClick={handleLogin} />
        <InputAutocomplete placeholder="Institution" items={institutions} />
      </form>
    </main>
  );
}
