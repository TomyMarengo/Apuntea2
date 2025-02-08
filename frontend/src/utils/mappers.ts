// src/utils/mappers.ts

import {
  FileType,
  NoteCategory,
  Note,
  Directory,
  Subject,
  Institution,
  Career,
  SubjectCareer,
  Review,
  User,
} from '../types';

export const mapApiUser = (apiUser: any): User => {
  return {
    id: apiUser.id,
    email: apiUser.email,
    username: apiUser.username,
    firstName: apiUser.firstName,
    lastName: apiUser.lastName,
    locale: apiUser.locale,
    status: apiUser.status,
    notificationsEnabled: apiUser.notificationsEnabled,
    selfUrl: apiUser.self,
    institutionUrl: apiUser.institution,
    careerUrl: apiUser.career,
    subjectFavoritesUrl: apiUser.subjectFavorites,
    noteFavoritesUrl: apiUser.noteFavorites,
    subjectsUrl: apiUser.subjects,
    reviewsReceivedUrl: apiUser.reviewsReceived,
    directoryFavoritesUrl: apiUser.directoryFavorites,
    followedByUrl: apiUser.followedBy,
    followingUrl: apiUser.following,
    followersUrl: apiUser.followers,
    profilePictureUrl: apiUser.profilePicture,
  };
};

export const mapApiNote = (apiNote: any): Note => {
  return {
    id: apiNote.id,
    name: apiNote.name,
    visible: apiNote.visible,
    fileType: apiNote.fileType as FileType,
    category: apiNote.category as NoteCategory,
    createdAt: apiNote.createdAt,
    lastModifiedAt: apiNote.lastModifiedAt,
    avgScore: apiNote.avgScore,
    parentId: apiNote.parentId,
    selfUrl: apiNote.self,
    fileUrl: apiNote.file,
    interactions: apiNote.interactions,
    interactionsUrl: apiNote.interactionsUri,
    ownerUrl: apiNote.owner,
    parentUrl: apiNote.parent,
    reviewsUrl: apiNote.reviews,
    subjectUrl: apiNote.subject,
  };
};

export const mapApiDirectory = (apiDirectory: any): Directory => {
  return {
    id: apiDirectory.id,
    name: apiDirectory.name,
    visible: apiDirectory.visible,
    iconColor: apiDirectory.iconColor,
    createdAt: apiDirectory.createdAt,
    lastModifiedAt: apiDirectory.lastModifiedAt,
    parentId: apiDirectory.parentId,
    selfUrl: apiDirectory.self,
    ownerUrl: apiDirectory.owner,
    parentUrl: apiDirectory.parent,
    subjectUrl: apiDirectory.subject,
  };
};

export const mapApiInstitution = (apiInstitution: any): Institution => {
  return {
    id: apiInstitution.id,
    name: apiInstitution.name,
    selfUrl: apiInstitution.self,
    careersUrl: apiInstitution.careers,
  };
};

export const mapApiCareer = (apiCareer: any): Career => {
  return {
    id: apiCareer.id,
    name: apiCareer.name,
    selfUrl: apiCareer.self,
    institutionUrl: apiCareer.institution,
    subjectsUrl: apiCareer.subjects,
    subjectCareersUrl: apiCareer.subjectCareers,
    subjectsNotInCareerUrl: apiCareer.subjectsNotInCareer,
  };
};

export const mapApiSubject = (apiSubject: any): Subject => {
  return {
    id: apiSubject.id,
    name: apiSubject.name,
    rootDirectoryId: apiSubject.rootDirectoryId,
    selfUrl: apiSubject.self,
    rootDirectoryUrl: apiSubject.rootDirectory,
  };
};

export const mapApiSubjectCareer = (apiSubjectCareer: any): SubjectCareer => {
  return {
    year: apiSubjectCareer.year,
    selfUrl: apiSubjectCareer.self,
    subjectUrl: apiSubjectCareer.subject,
    careerUrl: apiSubjectCareer.career,
  };
};

export const mapApiReview = (apiReview: any): Review => {
  return {
    userId: apiReview.userId,
    noteId: apiReview.noteId,
    content: apiReview.content,
    score: apiReview.score,
    createdAt: apiReview.createdAt,
    selfUrl: apiReview.self,
    noteUrl: apiReview.note,
    userUrl: apiReview.user,
  };
};
