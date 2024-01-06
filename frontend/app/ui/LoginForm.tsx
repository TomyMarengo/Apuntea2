"use client";
import { useFormState, useFormStatus } from "react-dom";
import { authenticate } from "../lib/actions";
import Input, { InputPassword } from "../ui/Input/Input";
import Button from "../ui/Button/Button";

const LoginForm = () => {
  const [errorMessage, dispatch] = useFormState(authenticate, undefined);

  return (
    <form action={dispatch} className="flex flex-col gap-4">
      <Input
        id="email"
        name="email"
        type="email"
        placeholder="apuntea@apuntea.com"
        required
      />
      <InputPassword
        id="password"
        name="password"
        placeholder="Enter password"
      />
      <LoginButton />
      <div
        className="flex h-8 items-end space-x-1"
        aria-live="polite"
        aria-atomic="true"
      >
        {errorMessage && (
          <>
            <p className="text-sm text-red-500">{errorMessage}</p>
          </>
        )}
      </div>
    </form>
  );
};

const LoginButton = () => {
  const { pending } = useFormStatus();

  return <Button aria-disabled={pending}>Log in</Button>;
};

export default LoginForm;
