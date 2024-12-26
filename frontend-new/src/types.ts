export interface Token {
  raw?: string;
  header?: any;
  payload?: any;
}

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  BANNED = 'BANNED',
}

export interface User {
  id?: string;
  email?: string;
  username?: string;
  locale?: string;
  status?: UserStatus;
  notificationsEnabled?: boolean;
  selfUrl?: string;
  institutionUrl?: string;
  careerUrl?: string;
  subjectFavoritesUrl?: string;
  noteFavoritesUrl?: string;
  noteGroupsUrl?: string;
  reviewsReceivedUrl?: string;
  directoryFavoritesUrl?: string;
  followingUrl?: string;
  profilePictureUrl?: string;
  career?: Career;
  institution?: Institution;
}

export interface Institution {
  id?: string;
  name?: string;
  selfUrl?: string;
  careersUrl?: string;
}

export interface Career {
  id?: string;
  name?: string;
  selfUrl?: string;
  institutionUrl?: string;
  subjectsUrl?: string;
  subjectCareersUrl?: string;
  subjectsNotInCareerUrl?: string;
}

export interface Subject {
  id?: string;
  name?: string;
  selfUrl?: string;
  rootDirectoryId?: string;
  rootDirectoryUrl?: string;
}

export enum FileType {
  PDF = 'pdf',
  DOCX = 'docx',
  PPTX = 'pptx',
  XLSX = 'xlsx',
  JPG = 'jpg',
  JPEG = 'jpeg',
  PNG = 'png',
  GIF = 'gif',
  MP4 = 'mp4',
  MP3 = 'mp3',
}

export enum Category {
  NOTE = 'NOTE',
  DIRECTORY = 'DIRECTORY',
  THEORY = 'THEORY',
  PRACTICE = 'PRACTICE',
  EXAM = 'EXAM',
  OTHER = 'OTHER',
}

export interface Note {
  id: string;
  name: string;
  visible?: boolean;
  fileType?: FileType;
  category?: Category;
  createdAt: string;
  lastModifiedAt: string;
  avgScore?: number;
  selfUrl?: string;
  fileUrl?: string;
  interactions?: number;
  interactionsUrl?: string;
  ownerUrl?: string;
  parentUrl?: string;
  reviewsUrl?: string;
  subjectUrl?: string;
}

export interface Directory {
  id: string;
  name: string;
  visible?: boolean;
  iconColor?: string;
  createdAt: string;
  lastModifiedAt: string;
  selfUrl?: string;
  ownerUrl?: string;
  parentUrl?: string;
  subjectUrl?: string;
}
