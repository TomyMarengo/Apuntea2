// src/pages/Search/SearchResultsTable.tsx

import ResultsTable from '../../components/ResultsTable';
import RowNote, { ColumnNote } from '../../components/Row/RowNote';
import RowDirectory, {
  ColumnDirectory,
} from '../../components/Row/RowDirectory';
import { Note, Directory, Column } from '../../types';

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
  // Initialize a Set to keep track of unique column IDs
  const columnSet = new Set<string>();

  // Initialize an array to hold the merged columns
  const mergedColumns: Column[] = [];

  // Helper function to add columns ensuring no duplicates
  const addColumns = (columns: Column[]) => {
    columns.forEach((col) => {
      if (!columnSet.has(col.id)) {
        columnSet.add(col.id);
        mergedColumns.push(col);
      }
    });
  };

  // Add columns from RowNote if notes are to be shown
  if (showNotes) {
    addColumns(ColumnNote);
  }

  // Add columns from RowDirectory if directories are to be shown
  if (showDirectories) {
    addColumns(ColumnDirectory);
  }

  return (
    <ResultsTable columns={mergedColumns}>
      {showNotes && notes.map((note) => <RowNote key={note.id} note={note} />)}
      {showDirectories &&
        directories.map((dir) => <RowDirectory key={dir.id} directory={dir} />)}
    </ResultsTable>
  );
};

export default SearchResultsTable;
