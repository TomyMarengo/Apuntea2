// src/utils/mappers.ts

import { FileType, Category, Note, Directory, Subject } from '../types';

export const mapApiNote = (apiNote: any): Note => {
  return {
    id: apiNote.id,
    name: apiNote.name,
    visible: apiNote.visible,
    fileType: apiNote.fileType as FileType,
    category: apiNote.category as Category,
    createdAt: apiNote.createdAt,
    lastModifiedAt: apiNote.lastModifiedAt,
    avgScore: apiNote.avgScore,
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
    selfUrl: apiDirectory.self,
    ownerUrl: apiDirectory.owner,
    parentUrl: apiDirectory.parent,
    subjectUrl: apiDirectory.subject,
  };
};

export const mapApiSubject = (apiSubject: any): Subject => {
  return {
    id: apiSubject.id,
    name: apiSubject.name,
    selfUrl: apiSubject.self,
    rootDirectoryUrl: apiSubject.rootDirectory,
    rootDirectoryId: apiSubject.rootDirectoryId,
  };
};
