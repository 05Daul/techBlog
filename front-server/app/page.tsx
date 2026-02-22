import Layout from "./component/layout/MainLayout";
import React from "react";


function MiniBlogContent() {
  return (
      <main>
        <Layout>
          <div style={{ padding: '0 10%', maxWidth: '1400px', margin: '0 auto' }}>
            <h1>test</h1>
          </div>
        </Layout>
      </main>
  );
}

export default function Home() {
  return <MiniBlogContent/>;
}