// src/hooks/useDirectoryBreadcrumb.ts

import { useEffect, useState } from 'react';
import { useLazyGetDirectoryQuery } from '../store/slices/directoriesApiSlice';
import { Directory } from '../types';

interface UseDirectoryBreadcrumbProps {
  currentDirectory: Directory;
}

const useDirectoryBreadcrumb = ({
  currentDirectory,
}: UseDirectoryBreadcrumbProps) => {
  const [getDirectory] = useLazyGetDirectoryQuery();
  const [breadcrumb, setBreadcrumb] = useState<Directory[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<boolean>(false);

  useEffect(() => {
    const fetchBreadcrumb = async () => {
      if (!currentDirectory) {
        setBreadcrumb([]);
        return;
      }

      setLoading(true);
      setError(false);
      const directories: Directory[] = [];

      let currentParentUrl: string | undefined = currentDirectory.parentUrl;

      try {
        while (currentParentUrl) {
          const response = await getDirectory({
            url: currentParentUrl,
          }).unwrap();
          directories.unshift(response);
          currentParentUrl = response.parentUrl;
        }
        setBreadcrumb(directories);
      } catch (err) {
        console.error('Error fetching breadcrumb:', err);
        setError(true);
      } finally {
        setLoading(false);
      }
    };

    fetchBreadcrumb();
  }, [currentDirectory, getDirectory]);

  return { breadcrumb, loading, error };
};

export default useDirectoryBreadcrumb;
