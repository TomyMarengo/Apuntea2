// src/types.ts

export interface Header {
  alg: string;
  typ: string;
}

export interface Payload {
  sub: string;
  iat: number;
  exp: number;
  iss: string;
  jti: string;
  tokenType: string;
  userId: string;
  authorities: string[];
}

export interface Token {
  raw?: string;
  header?: Header;
  payload?: Payload;
}

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  BANNED = 'BANNED',
}

export enum Locale {
  EN = 'en',
  ES = 'es',
}

export interface User {
  id: string;
  email: string;
  username?: string;
  firstName?: string;
  lastName?: string;
  locale?: Locale;
  status?: UserStatus;
  notificationsEnabled?: boolean;
  selfUrl: string;
  institutionUrl: string;
  careerUrl: string;
  subjectFavoritesUrl?: string;
  noteFavoritesUrl?: string;
  subjectsUrl?: string;
  reviewsReceivedUrl?: string;
  directoryFavoritesUrl?: string;
  followingUrl?: string;
  profilePictureUrl: string;
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

export interface SubjectCareer {
  year: number;
  selfUrl: string;
  careerUrl: string;
  subjectUrl: string;
}

export interface SubjectWithCareer {
  subjectId: string;
  name: string;
  year: number;
  subjectUrl?: string;
  subjectCareerUrl?: string;
  careerUrl?: string;
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
  visible: boolean;
  fileType?: FileType;
  category?: Category;
  createdAt: string;
  lastModifiedAt: string;
  avgScore: number;
  selfUrl: string;
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

export interface Review {
  userId: string;
  noteId: string;
  content: string;
  score: number;
  createdAt: string;
  selfUrl: string;
  noteUrl: string;
  userUrl: string;
}
