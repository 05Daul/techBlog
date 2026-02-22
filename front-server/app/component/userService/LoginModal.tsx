import React, { useState, FormEvent } from "react";
import styles from "../../styles/userService/LoginModal.module.css";
import { login } from "../../api/userService/user";
import { useRouter } from "next/router";
import Link from "next/link";

interface LoginModalProps {
  onClose: () => void;
  onLoginSuccess: () => void;
}

export default function LoginModal({ onClose, onLoginSuccess }: LoginModalProps) {
  const router = useRouter();
  const [userSignId, setUserSignId] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setError(null);

    try {
      const response = await login({ userSignId, password });

      console.log("🔥 [LoginModal] 로그인 응답:", response);
      console.log("🔥 [LoginModal] response.profile_img:", (response as any).profile_img);

      // 토큰 및 사용자 정보 저장
      const expiresAt = Date.now() + 12 * 60 * 60 * 1000; // 12시간
      localStorage.setItem("accessTokenExpiresAt", expiresAt.toString());
      localStorage.setItem("accessToken", response.accessToken);
      localStorage.setItem("refreshToken", response.refreshToken);
      localStorage.setItem("userSignId", response.userId);


      alert("환영합니다!");
      onLoginSuccess();
      onClose();

    } catch (err: any) {
      console.error(err);
      setError("로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
    }
  }

  return (
      <div className={styles.overlay} onClick={onClose}>
        <div className={styles.modalBox} onClick={e => e.stopPropagation()}>

          {/* 왼쪽: 감성 일러스트 영역 */}
          <div className={styles.leftSection}>
            <div className={styles.illustration}>
              <span role="img" aria-label="sparkles" style={{ fontSize: "4rem" }}>✨</span>
            </div>
            <h2 className={styles.welcomeText}>다시 만나서 반가워요</h2>
            <p className={styles.welcomeSub}>오늘도 소중한 순간을 기록해볼까요?</p>
          </div>

          {/* 오른쪽: 로그인 폼 */}
          <div className={styles.rightSection}>
            <button className={styles.closeButton} onClick={onClose}>×</button>

            <h2 className={styles.title}>로그인</h2>
            <form onSubmit={handleSubmit} className={styles.form}>
              <input
                  type="text"
                  className={styles.input}
                  placeholder="아이디"
                  value={userSignId}
                  onChange={e => setUserSignId(e.target.value)}
              />
              <input
                  type="password"
                  className={styles.input}
                  placeholder="비밀번호"
                  value={password}
                  onChange={e => setPassword(e.target.value)}
              />

              {error && <div className={styles.errorMessage}>{error}</div>}

              <button type="submit" className={styles.loginButton}>
                로그인
              </button>
            </form>

            <div className={styles.footer}>
              아직 계정이 없나요? <Link href="/signup" className={styles.signupLink} onClick={onClose}>회원가입</Link>
            </div>
          </div>
        </div>
      </div>
  );
}