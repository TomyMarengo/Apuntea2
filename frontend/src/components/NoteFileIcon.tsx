import { FileType } from '../types';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import DescriptionIcon from '@mui/icons-material/Description';
import ImageIcon from '@mui/icons-material/Image';
import MovieIcon from '@mui/icons-material/Movie';
import MusicNoteIcon from '@mui/icons-material/MusicNote';

interface NoteFileIconProps {
  fileType?: FileType;
  size?: number;
}

const NoteFileIcon: React.FC<NoteFileIconProps> = ({ fileType, size }) => {
  if (!fileType)
    return <InsertDriveFileIcon sx={{ fontSize: size }} color="primary" />;

  switch (fileType.toLowerCase()) {
    case 'pdf':
      return <PictureAsPdfIcon sx={{ fontSize: size }} color="primary" />;
    case 'doc':
    case 'docx':
    case 'ppt':
    case 'pptx':
    case 'xls':
    case 'xlsx':
      return <DescriptionIcon sx={{ fontSize: size }} color="primary" />;
    case 'jpg':
    case 'jpeg':
    case 'png':
    case 'gif':
      return <ImageIcon sx={{ fontSize: size }} color="primary" />;
    case 'mp4':
      return <MovieIcon sx={{ fontSize: size }} color="primary" />;
    case 'mp3':
      return <MusicNoteIcon sx={{ fontSize: size }} color="primary" />;
    default:
      return <InsertDriveFileIcon sx={{ fontSize: size }} color="primary" />;
  }
};

export default NoteFileIcon;
