// 테마 토글 및 상태 유지 스크립트
(function() {
  // 페이지 로드 전 깜빡임 방지를 위해 즉시 실행
  const theme = localStorage.getItem('theme') || 'dark';
  if (theme === 'light') {
    document.documentElement.classList.add('light-mode');
    document.addEventListener('DOMContentLoaded', () => document.body.classList.add('light-mode'));
  }
})();

window.addEventListener('DOMContentLoaded', () => {
  const toggleBtn = document.getElementById('theme-toggle');
  if (!toggleBtn) return;

  const updateIcon = (isLight) => {
    const icon = toggleBtn.querySelector('i');
    if (icon) {
      icon.className = isLight ? 'fa-solid fa-sun' : 'fa-solid fa-moon';
    }
  };

  // 초기 아이콘 설정
  updateIcon(document.body.classList.contains('light-mode'));

  toggleBtn.addEventListener('click', () => {
    document.body.classList.toggle('light-mode');
    const isLight = document.body.classList.contains('light-mode');
    
    // 상태 저장
    localStorage.setItem('theme', isLight ? 'light' : 'dark');
    
    // 아이콘 업데이트
    updateIcon(isLight);
    
    // 다른 요소들에 대한 추가 처리가 필요하다면 여기에 작성
  });
});
