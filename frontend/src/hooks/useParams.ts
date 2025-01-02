// src/hooks/useParams.ts

import { useSearchParams } from 'react-router-dom';

export interface Params {
  [key: string]: string;
}

interface UseParamsReturn {
  params: Params;
  query: string;
}

/**
 * A minimal custom hook that reads all search params as an object
 * and also returns the entire query string.
 */
export default function useParams(): UseParamsReturn {
  const [searchParams] = useSearchParams();
  const params: Params = {};

  for (const [key, value] of searchParams.entries()) {
    params[key] = value;
  }

  const query = new URLSearchParams(params).toString() || '';
  return { params, query };
}
