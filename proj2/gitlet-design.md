# Gitlet Design Document

**Name**: lzh

## Classes and Data Structures

### class 1 

#### Blob 

1. blob 
2. getSha1 
3. getFilename
4. addBlobToStorage
5. savaBlobToLinkListStagingArea
6. getSha1ByFile
7. readContentFromFile
8. calculateSHA1


### Class 2

#### Branch

1. Branch 
2. findMainBranch 
3. createBranchFile
4. writeSha1ToBranchFile
5. getName
6. checkupCommitIdByBranchName

### class 3

#### Commit

1. Commit
2. combineTwoMap
3. getSha1OfCommit
4. getContentOfFIleToGetSha1
5. getMessage
6. getDate
7. getFroMatDate
8. getParent
9. savaCommit
10. saveCommitToFile
11. getBlobPoints
12. deserializeCommitBySha1
13. getMapFromStagingArea
14. getNameFromRmArea
15. findLatestCommitSha1
16. findCommitByCommitSha1
17. checkTheFileBlobPointsOrNot
18. updateDeletion
19. updatePoints



## Algorithms

## Persistence

