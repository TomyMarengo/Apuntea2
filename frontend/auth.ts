import NextAuth from "next-auth";
import Credentials from "next-auth/providers/credentials";
import { authConfig } from "./auth.config";
import { z } from "zod";
import type { User } from "@/lib/definitions";
import bcrypt from "bcrypt";

async function getUser(
  email: string,
  password: string
): Promise<User | undefined> {
  /* TODO: Devolver el JWT y el User*/
  return undefined;
}

export const { auth, signIn, signOut } = NextAuth({
  ...authConfig,
  providers: [
    Credentials({
      async authorize(credentials) {
        const parsedCredentials = z
          .object({ email: z.string().email(), password: z.string().min(6) })
          .safeParse(credentials);

        if (parsedCredentials.success) {
          const { email, password } = parsedCredentials.data;
          const user = await getUser(email, bcrypt.hashSync(password, 10));
          if (!user) return null;
          return user;
        }

        console.log("Invalid credentials");
        return null;
      },
    }),
  ],
});
