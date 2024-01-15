import { useSearchParams } from 'react-router-dom';

export default function useParams() {
  const [searchParams] = useSearchParams();
  const params = {};

  for (const [key, value] of searchParams.entries()) {
    params[key] = value;
  }

  return params;
}
