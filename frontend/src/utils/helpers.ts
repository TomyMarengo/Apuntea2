import { Token } from '../types';

export function decode(t: string | null | undefined): Token {
  let token: Token = {};
  if (t === null || t === undefined) return token;
  token.raw = t;
  token.header = JSON.parse(window.atob(t.split('.')[0]));
  token.payload = JSON.parse(window.atob(t.split('.')[1]));
  return token;
}

export function isUuid(uuid: string): boolean {
  const regex =
    /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/;
  return regex.test(uuid);
}

export function serializeFormQuery(params: Record<string, any>): string {
  // remove params with empty values or null or undefined
  Object.keys(params).forEach((key) => {
    if (params[key] === '' || params[key] === null || params[key] === undefined)
      delete params[key];
  });
  const query = new URLSearchParams(params).toString() || '';
  return query;
}
