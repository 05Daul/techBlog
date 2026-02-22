import styles from "../../../styles/layout/layout.module.css";
import Link from "next/link";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import LoginModal from "../userService/LoginModal";
import React from "react";

// 🟢 [추가] ProfileDropdownProps 정의
interface ProfileDropdownProps {
  onClose: () => void;
  onNavigate: (path: string) => void;
  userSignId: string;
}

// 🟢 [추가] ProfileDropdown 컴포넌트
const ProfileDropdown = ({onClose, onNavigate, userSignId}: ProfileDropdownProps) => {

  const handleLinkClick = (path: string) => {
    onNavigate(path);
    onClose();
  };

  return (
      <div className={styles.profileDropdown}>
        <div className={styles.dropdownHeader}>
          <strong>{userSignId}</strong>
        </div>
        <button onClick={() => handleLinkClick(`/my-posts/${userSignId}`)}
                className={styles.dropdownItem}>
          내 게시물
        </button>
        <button onClick={() => handleLinkClick('/setting')} className={styles.dropdownItem}>
          설정 (이미지/비밀번호)
        </button>
      </div>
  );
};


export default function Topbar() {
  const router = useRouter();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showFriendModal, setShowFriendModal] = useState(false);
  const [profileImg, setProfileImg] = useState('');
  const [userSignId, setUserSignId] = useState('');
  const [imageLoadError, setImageLoadError] = useState(false);

  // 🟢 [추가] 드롭다운 메뉴 상태
  const [showProfileDropdown, setShowProfileDropdown] = useState(false);

  // 초기 로그인 상태 확인
  useEffect(() => {
    checkLoginStatus();
    setImageLoadError(false);
  }, []);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const expiresAt = localStorage.getItem("accessTokenExpiresAt");

    if (!token || !expiresAt) return;

    const remainingTime = Number(expiresAt) - Date.now();

    console.log("⏰ 자동 로그아웃까지(ms):", remainingTime);

    if (remainingTime <= 0) {
      forceLogout();
      return;
    }

    const timer = setTimeout(() => {
      forceLogout();
    }, remainingTime);

    return () => clearTimeout(timer);
  }, []);


  const checkLoginStatus = () => {
    const token = localStorage.getItem("accessToken");
    const expiresAt = localStorage.getItem("accessTokenExpiresAt");
    const userId = localStorage.getItem("userSignId");
    const profile = localStorage.getItem("profile_img");

    if (!token || !expiresAt) {
      setIsLoggedIn(false);
      return;
    }

    if (Date.now() > Number(expiresAt)) {
      forceLogout();
      return;
    }

    setIsLoggedIn(true);
    setUserSignId(userId || '');
    setProfileImg(profile || '');
  };
  const handleLoginSuccess = () => {
    checkLoginStatus();
    setShowLoginModal(false);
  };

  const handleLogout = () => {

    localStorage.clear();
    setIsLoggedIn(false);
    setProfileImg('');
    setUserSignId('');
    alert("로그아웃 되었습니다.");
    router.push("/");

  };
  const forceLogout = () => {
    console.warn("⏳ 토큰 만료 → 자동 로그아웃");

    localStorage.clear();
    setIsLoggedIn(false);
    setProfileImg('');
    setUserSignId('');
    setShowProfileDropdown(false);

    alert("로그인 시간이 만료되었습니다.");
    router.replace("/");
  };
  const handleProfileClick = () => {
    setShowProfileDropdown(prev => !prev);
  };

  const handleNavigate = (path: string) => {
    router.push(path);
    setShowProfileDropdown(false); // 네비게이션 후 닫기
  };


  // 로그인한 사용자 signId 가져오기
  const currentUserSignId = typeof window !== "undefined"
      ? localStorage.getItem("userSignId") || ""
      : "";

  return (
      <>
        <header className={styles.topbar}>
          <Link href="/" className={styles.leftSection}>
            MomenTory
          </Link>

          <nav className={styles.rightSection}>
            {isLoggedIn ? (
                <>
                  <Link href="/community" className={`${styles.rightItem} ${styles.navLink}`}>
                    커뮤니티
                  </Link>


                  <Link href="/page" className={`${styles.rightItem} ${styles.writeButton}`}>
                    채팅
                  </Link>

                  <Link href="/write" className={`${styles.rightItem} ${styles.writeButton}`}>
                    Log 작성
                  </Link>

         {/*         <div className={styles.profileContainer}>  드롭다운 위치 지정을 위한 컨테이너
                    <div
                        className={styles.profileSection}
                        onClick={handleProfileClick} // 🟢 토글 핸들러 사용
                        style={{cursor: 'pointer'}}
                    >
                       프로필 이미지 또는 아이디 첫글자
                      {profileImg && !imageLoadError ? (
                          <img
                              src={getImageUrl(profileImg)}
                              alt="프로필"
                              className={styles.profileImage}
                              onError={(e) => {
                                console.error('❌ 이미지 로드 실패:', getImageUrl(profileImg));
                                setImageLoadError(true);
                                e.currentTarget.style.display = 'none';
                              }}
                              onLoad={() => {
                                console.log('✅ 이미지 로드 성공:', getImageUrl(profileImg));
                                setImageLoadError(false);
                              }}
                          />
                      ) : (
                          <div className={styles.profileCircle}>
                            {getInitial(userSignId)}
                          </div>
                      )}
                    </div>

                     🟢 드롭다운 렌더링
                    {showProfileDropdown && (
                        <ProfileDropdown
                            onClose={() => setShowProfileDropdown(false)}
                            onNavigate={handleNavigate}
                            userSignId={userSignId}
                        />
                    )}
                  </div>*/}


                  <div className={styles.rightItem} onClick={handleLogout}
                       style={{cursor: 'pointer'}}>
                    로그아웃
                  </div>
                </>
            ) : (
                <div
                    className={styles.rightItem}
                    style={{cursor: "pointer"}}
                    onClick={() => setShowLoginModal(true)}
                >
                  로그인
                </div>
            )}
          </nav>
        </header>

        {showLoginModal && (
            <LoginModal
                onClose={() => setShowLoginModal(false)}
                onLoginSuccess={handleLoginSuccess}
            />
        )}
      </>
  );
}