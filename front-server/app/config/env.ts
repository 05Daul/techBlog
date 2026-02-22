export const AUTH_API = process.env.NEXT_PUBLIC_AUTH_API
if (!AUTH_API) throw new Error('NEXT_PUBLIC_AUTH_API 누락!');

export const USER_API = process.env.NEXT_PUBLIC_AUTH_API
if (!USER_API) throw new Error('NEXT_PUBLIC_USER_API 누락!');

export const BLOG_API = process.env.NEXT_PUBLIC_AUTH_API
if (!BLOG_API) throw new Error('NEXT_PUBLIC_BLOG_API 누락!');




