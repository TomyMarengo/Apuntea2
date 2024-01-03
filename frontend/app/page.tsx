'use client';

import Button from "@/ui/Button";
import Input from "@/ui/Input";
import DoubleCard from "./ui/DoubleCard";
import { useState } from "react";

export default function Home() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    // TODO
  }

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24 ">
      Apuntea
      <DoubleCard title="Login">
        <form>
          <Input type="email" placeholder="Email"
            onChange={(e) => setEmail(e.target.value)} />
          <Input type="password" placeholder="Password" 
            onChange={(e) => setPassword(e.target.value)} />
          <Button label="Button" onClick={handleLogin}/>
        </form>
      </DoubleCard>
    </main>
  );
}
