// 캐시 이름 설정
const CACHE_NAME = 'java-quiz-v1';
const urlsToCache = [
  '/java-interview-quiz/',
  '/java-interview-quiz/index.html',
  '/java-interview-quiz/style.css',
  '/java-interview-quiz/scripts/script.js',
  '/java-interview-quiz/scripts/service-worker.js',
  '/java-interview-quiz/manifest.json',
  '/java-interview-quiz/data/basic.json',
  '/java-interview-quiz/data/collections.json',
  '/java-interview-quiz/data/database.json',
  '/java-interview-quiz/data/designpattern.json',
  '/java-interview-quiz/data/exception.json',
  '/java-interview-quiz/data/jvm.json',
  '/java-interview-quiz/data/network.json',
  '/java-interview-quiz/data/oop.json',
  '/java-interview-quiz/data/spring.json',
  '/java-interview-quiz/data/thread.json',
  '/java-interview-quiz/icons/icon-72x72.png',
  '/java-interview-quiz/icons/icon-192x192.png',
  '/java-interview-quiz/icons/icon-512x512.png'
];

// 설치 이벤트
self.addEventListener('install', (event) => {
  console.log('Service Worker 설치 중...');
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => {
        console.log('캐시 파일들을 저장 중...');
        return cache.addAll(urlsToCache);
      })
      .catch((error) => {
        console.error('캐시 저장 중 오류:', error);
      })
  );
});

// 활성화 이벤트
self.addEventListener('activate', (event) => {
  console.log('Service Worker 활성화 중...');
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cacheName) => {
          if (cacheName !== CACHE_NAME) {
            console.log('이전 캐시 삭제:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
});

// 네트워크 요청 가로채기
self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request)
      .then((response) => {
        // 캐시에서 찾으면 캐시된 버전 반환
        if (response) {
          console.log('캐시에서 제공:', event.request.url);
          return response;
        }
        
        // 캐시에 없으면 네트워크에서 가져오기
        console.log('네트워크에서 가져오기:', event.request.url);
        return fetch(event.request)
          .then((response) => {
            // 유효한 응답인지 확인
            if (!response || response.status !== 200 || response.type !== 'basic') {
              return response;
            }
            
            // 응답을 복제하여 캐시에 저장
            const responseToCache = response.clone();
            caches.open(CACHE_NAME)
              .then((cache) => {
                cache.put(event.request, responseToCache);
              });
            
            return response;
          })
          .catch((error) => {
            console.error('네트워크 요청 실패:', error);
            // 오프라인 시 기본 페이지 반환 (선택사항)
            if (event.request.destination === 'document') {
              return caches.match('/java-interview-quiz/index.html');
            }
          });
      })
  );
});

// 백그라운드 동기화 (선택사항)
self.addEventListener('sync', (event) => {
  if (event.tag === 'background-sync') {
    console.log('백그라운드 동기화 실행');
    // 필요시 백그라운드 작업 추가
  }
});

// 푸시 알림 (선택사항)
self.addEventListener('push', (event) => {
  console.log('푸시 알림 수신:', event);
  // 필요시 푸시 알림 처리 로직 추가
});
