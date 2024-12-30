// src/components/ResultsTable.tsx

import {
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Paper,
} from '@mui/material';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';

export interface Column {
  id: string;
  label: string;
  align?: 'right' | 'left' | 'center';
}

interface ResultsTableProps {
  columns: Column[];
  children: React.ReactNode;
}

const ResultsTable: FC<ResultsTableProps> = ({ columns, children }) => {
  const { t } = useTranslation();

  return (
    <Paper sx={{ mt: 3 }}>
      <Table>
        <TableHead>
          <TableRow>
            {columns.map((column) => (
              <TableCell key={column.id} align={column.align || 'left'}>
                {t(column.label)}
              </TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>{children}</TableBody>
      </Table>
    </Paper>
  );
};

export default ResultsTable;
