import type { NextAuthConfig } from "next-auth";
import Credentials from "next-auth/providers/credentials";
import { z } from "zod";
import bcrypt from "bcrypt";

export const authConfig = {
  pages: {
    signIn: "/login",
  },
  callbacks: {
    authorized({ auth, request: { nextUrl } }) {
      console.log("auth", auth);
      const isLoggedIn = !!auth?.user;
      const isOnIndex = nextUrl.pathname.startsWith("/");
      if (isOnIndex) {
        if (isLoggedIn) return true;
        return false; // Redirect unauthenticated users to login page
      } else if (isLoggedIn) {
        return Response.redirect(new URL("/", nextUrl));
      }
      return true;
    },
  },
  providers: [
    Credentials({
      name: "credentials",
      id: "credentials",
      credentials: {
        email: {},
        password: {},
      },
      async authorize(credentials) {
        const parsedCredentials = z
          .object({ email: z.string().email(), password: z.string().min(6) })
          .safeParse(credentials);

        if (parsedCredentials.success) {
          const { email, password } = parsedCredentials.data;
          const basicAuth = btoa(`${email}:${password}`);
          const response = await fetch(
            "http://localhost:8080/paw-2023b-12/tokens",
            {
              method: "POST",
              headers: {
                Authorization: `Basic ${basicAuth}`,
                "Content-Type": "application/json",
              },
            }
          );

          if (response.ok) {
            const accessToken = response.headers.get("access-token");

            if (accessToken) {
              /* TODO: get user, and extend User type */
              const user = {
                id: accessToken,
                name: "John Doe",
                email: "johndoe@gmail.com",
              };
              return { ...user };
            } else {
              return Promise.reject(
                new Error("Access token not found in headers")
              );
            }
          } else {
            const data = await response.json();
            return Promise.reject(
              new Error(data?.errors || `Server error: ${response.statusText}`)
            );
          }
        }
        return Promise.reject(new Error("Invalid credentials"));
      },
    }),
  ],
  session: {
    strategy: "jwt",
  },
} satisfies NextAuthConfig;
