const darkModeToggle = document.getElementById('darkModeToggle');
const modeIcon = document.getElementById('darkModeIcon');

darkModeToggle.addEventListener('click', () => {
	if (document.documentElement.getAttribute('data-bs-theme') === 'dark') {
		modeIcon.src = '../svg/moon.svg';
		document.documentElement.setAttribute('data-bs-theme', 'light');
	}
	else {
		modeIcon.src = '../svg/sun.svg';
		document.documentElement.setAttribute('data-bs-theme', 'dark');
	}
});