// src/components/NoteFileIcon.tsx

import ImageIcon from '@mui/icons-material/Image';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import MovieIcon from '@mui/icons-material/Movie';
import MusicNoteIcon from '@mui/icons-material/MusicNote';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import React from 'react';

import { FileType } from '../types';

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
    case 'jpg':
    case 'jpeg':
    case 'png':
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
