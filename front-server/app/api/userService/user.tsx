import {AUTH_API} from "@/app/config/env";
import {LoginDTO} from "@/app/types/userService/auth";

export async function login(loginDto: LoginDTO): Promise<{
  userId: string;
  role: string;
  refreshToken: string;
  accessToken: string;
}> {
  const response = await fetch(`${AUTH_API}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(loginDto),
  });

  if (!response.ok) {
    throw new Error(`로그인 실패: ${response.status}`);
  }

  const authHeader = response.headers.get("Authorization");
  const accessToken = authHeader?.startsWith("Bearer ") ? authHeader.substring(7) : "";
  const body = await response.json();

  return {
    userId: body.userId,
    role: body.role,
    refreshToken: body.refreshToken,
    accessToken: body.accessToken
  };
}