import { Switch } from '@mui/material';

interface DarkModeToggleProps {
  isDarkMode: boolean;
  onToggle: () => void;
}

export default function DarkModeToggle({
  isDarkMode,
  onToggle,
}: DarkModeToggleProps) {
  return <Switch checked={isDarkMode} onChange={onToggle} color="default" />;
}
