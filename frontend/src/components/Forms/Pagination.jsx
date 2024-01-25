import clsx from 'clsx';
import { useSearchParams, NavLink } from 'react-router-dom';
import { generatePagination } from '../../functions/pagination';
import { ChevronLeftIcon, ChevronRightIcon } from '../Utils/Icons';
import { Trans } from 'react-i18next';

const Pagination = ({ totalPages, currentPage, pageSize, totalCount, dataLength }) => {
  const [searchParams] = useSearchParams();
  const allPages = generatePagination(currentPage, totalPages);

  const createPageURL = (page) => {
    const params = new URLSearchParams(searchParams);
    params.set('page', page);
    return '?' + params.toString();
  };

  return (
    <div className="flex flex-col gap-3 justify-center items-center">
      <div className="inline-flex">
        <PaginationArrow direction="left" href={createPageURL(currentPage - 1)} isDisabled={currentPage <= 1} />

        <div className="flex -space-x-px">
          {allPages.map((page, index) => {
            let position = undefined;

            if (index === 0) position = 'first';
            if (index === allPages.length - 1) position = 'last';
            if (allPages.length === 1) position = 'single';
            if (page === '...') position = 'middle';

            return (
              <PaginationNumber
                key={page}
                href={createPageURL(page)}
                page={page}
                position={position}
                isActive={currentPage === page}
              />
            );
          })}
        </div>

        <PaginationArrow
          direction="right"
          href={createPageURL(currentPage + 1)}
          isDisabled={currentPage >= totalPages}
        />
      </div>
      <span className="italic text-sm">
        <Trans
          i18nKey="pages.search.totalCount"
          values={{ from: currentPage, to: currentPage + Math.min(pageSize, dataLength) - 1, totalCount }}
          components={[
            <span key="0" className="text-dark-text font-bold"></span>,
            <span key="1" className="text-dark-text font-bold"></span>,
            <span key="2" className="text-dark-text font-bold"></span>,
          ]}
        />
      </span>
    </div>
  );
};

export default Pagination;

function PaginationNumber({ page, href, isActive, position /* 'first' | 'last' | 'middle' | 'single'*/ }) {
  const className = clsx('flex h-10 w-10 items-center justify-center text-sm border', {
    'rounded-l-md': position === 'first' || position === 'single',
    'rounded-r-md': position === 'last' || position === 'single',
    'z-10 bg-pri border-pri text-bg': isActive,
    'hover:bg-dark-bg text-pri': !isActive && position !== 'middle',
    'text-text': position === 'middle',
  });

  return isActive || position === 'middle' ? (
    <div className={className}>{page}</div>
  ) : (
    <NavLink to={href} className={className}>
      {page}
    </NavLink>
  );
}

function PaginationArrow({ href, direction, isDisabled }) {
  const className = clsx('flex h-10 w-10 items-center justify-center rounded-md border', {
    'pointer-events-none bg-text/10 fill-text/50': isDisabled,
    'hover:bg-dark-bg fill-pri': !isDisabled,
    'mr-2 md:mr-4': direction === 'left',
    'ml-2 md:ml-4': direction === 'right',
  });

  const icon = direction === 'left' ? <ChevronLeftIcon className="w-4" /> : <ChevronRightIcon className="w-4" />;

  return isDisabled ? (
    <div className={className}>{icon}</div>
  ) : (
    <NavLink className={className} to={href}>
      {icon}
    </NavLink>
  );
}
