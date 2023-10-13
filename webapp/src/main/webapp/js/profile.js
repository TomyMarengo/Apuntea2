const rootDirs = document.querySelectorAll('.root-dir');
const rootDirLists = document.querySelectorAll('.root-dir-list');

rootDirs.forEach(function(rootDir, index) {
    rootDir.addEventListener('click', function(event) {
        event.preventDefault();

        rootDirLists.forEach(function (tabContent) {
            tabContent.classList.remove('show', 'active');
        });

        rootDirLists[index].classList.add('show', 'active');

        rootDirs.forEach(function (link) {
            link.classList.remove('active');
        });

        rootDirs[index].classList.add('active');
    });
});

// TODO: Change active between favorites and my notes
const favoriteDirs = document.querySelectorAll('.favorite-dir');
const favoriteDirLists = document.querySelectorAll('.favorite-dir-list');

