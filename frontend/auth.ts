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
      name: "credentials",
      id: "credentials",
      credentials: {
        email: {},
        password: {},
      },
      async authorize(credentials, req) {
        const parsedCredentials = z
          .object({ email: z.string().email(), password: z.string().min(6) })
          .safeParse(credentials);

        if (parsedCredentials.success) {
          const { email, password } = parsedCredentials.data;
          const response = await fetch(
            "http://localhost:8080/paw-2023b-12/tokens",
            {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({ email, password }),
            }
          );
          const data = await response.json();
          if (!user) return null;
          return user;
        }

        console.log("Invalid credentials");
        return null;
      },
    }),
  ],
  session: {
    strategy: "jwt",
  },
  callbacks: {
    async jwt({ token, user, account, profile }) {
      return token;
    },
  },
});
