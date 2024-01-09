export type User = {
  id: string;
  name: string;
  email: string;
  password: string;
};

export type Note = {
  self: string;
  name?: string;
  fileType: string;
  category?: string;
  visible?: boolean;
  interactions?: number;
  createdAt?: Date;
  lastModifiedAt?: Date;
  subject?: string;
  parent?: string;
  createdBy?: string;
  file?: string;
};

export type Directory = {
  self: string;
  name?: string;
  visible?: boolean;
  iconColor?: string;
  createdAt?: Date;
  lastModifiedAt?: Date;
  subject?: string;
  parent?: string;
  createdBy?: string;
};

export interface Option {
  readonly label: string;
  readonly value: string;
}

export enum FileType {
  JPG = "jpg",
  JPEG = "jpeg",
  PNG = "png",
  PDF = "pdf",
  MP3 = "mp3",
  MP4 = "mp4",
}
