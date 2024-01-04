"use client";
import { useState } from "react";
import Button from "@/ui/Button/Button";
import Input from "@/ui/Input/Input";
import DoubleCard from "./ui/DoubleCard";
import InputPassword from "./ui/Input/InputPassword";

export default function Home() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = () => {
    // TODO
  };

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24 ">
      Apuntea
      <DoubleCard title="Login">
        <form>
          <Input
            type="email"
            placeholder="Email"
            onChange={(e) => setEmail(e.target.value)}
          />
          <InputPassword onChange={(e) => setPassword(e.target.value)} />
          <Button label="Button" onClick={handleLogin} />
        </form>
      </DoubleCard>
    </main>
  );
}
