// src/pages/Search/SearchResultsTable.tsx

import { useMediaQuery, useTheme } from '@mui/material';
import React from 'react';

import ResultsTable from '../../components/ResultsTable';
import RowDirectory from '../../components/Row/RowDirectory';
import RowNote from '../../components/Row/RowNote';
import {
  Note,
  Directory,
  Column,
  ColumnNote,
  ColumnDirectory,
} from '../../types';

interface Props {
  showNotes: boolean;
  showDirectories: boolean;
  notes: Note[];
  directories: Directory[];
}

const SearchResultsTable: React.FC<Props> = ({
  showNotes,
  showDirectories,
  notes,
  directories,
}) => {
  const theme = useTheme();

  // Breakpoints
  const isXs = useMediaQuery(theme.breakpoints.down('sm')); // <600px
  const isSm = useMediaQuery(theme.breakpoints.between('sm', 'md')); // 600px - 960px
  const isMd = useMediaQuery(theme.breakpoints.between('md', 'lg')); // 960px - 1280px
  const columns = showNotes ? ColumnNote : ColumnDirectory;

  // Determine the maximum number of visible columns based on screen size
  let maxVisibleColumns = columns.length; // Default: show all

  if (isXs) {
    maxVisibleColumns = 2; // e.g., 'actions' + one more
  } else if (isSm) {
    maxVisibleColumns = 3; // 'actions' + two more
  } else if (isMd) {
    maxVisibleColumns = 4; // 'actions' + three more
  }

  // Sort columns by priority (ascending order)
  const sortedColumns = [...columns].sort((a, b) => a.priority - b.priority);

  // Slice the columns to show based on the maximum allowed
  const columnsToShow = sortedColumns.slice(0, maxVisibleColumns);

  // Get the columnsToShow but in the order of columns
  columnsToShow.sort((a, b) => {
    return (
      columns.findIndex((col) => col.id === a.id) -
      columns.findIndex((col) => col.id === b.id)
    );
  });

  return (
    <ResultsTable columns={columnsToShow}>
      {showNotes &&
        notes.map((note) => (
          <RowNote key={note.id} note={note} columnsToShow={columnsToShow} />
        ))}
      {showDirectories &&
        directories.map((dir) => (
          <RowDirectory
            key={dir.id}
            directory={dir}
            columnsToShow={columnsToShow}
          />
        ))}
    </ResultsTable>
  );
};

export default SearchResultsTable;
