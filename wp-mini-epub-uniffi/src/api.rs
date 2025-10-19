use crate::error::WPEpubError;
use crate::runtime::{HTTP_CLIENT, TOKIO_RUNTIME};
use std::path::PathBuf;
#[cfg(not(target_arch = "wasm32"))]
use wp_mini_epub::download_story_to_file as wp_download_story_to_file;
use wp_mini_epub::login as wp_login;
use once_cell::sync::Lazy;

#[uniffi::export]
pub fn init_runtime() {
    Lazy::force(&TOKIO_RUNTIME);
}

#[uniffi::export]
pub async fn login(username: String, password: String) -> Result<(), WPEpubError> {
    TOKIO_RUNTIME
        .spawn(async move {
            wp_login(&HTTP_CLIENT, &username, &password)
                .await
                .map_err(WPEpubError::from)
        })
        .await
        .map_err(|e| WPEpubError::IoError(e.to_string()))?
}

#[derive(uniffi::Record)]
pub struct StoryFileResult {
    pub title: String,
    pub path: String,
}

#[uniffi::export]
pub async fn download_story_to_file(
    story_id: u64,
    embed_images: bool,
    concurrent_requests: u32,
    output_path: String,
) -> Result<StoryFileResult, WPEpubError> {
    let path = PathBuf::from(output_path);

    TOKIO_RUNTIME
        .spawn(async move {
            wp_download_story_to_file(
                &HTTP_CLIENT,
                story_id,
                embed_images,
                concurrent_requests as usize,
                &path,
                None,
            )
            .await
            .map_err(WPEpubError::from)
            .map(|result| StoryFileResult {
                title: result.sanitized_title,
                path: result.epub_response.to_string_lossy().to_string(),
            })
        })
        .await
        .map_err(|e| WPEpubError::IoError(e.to_string()))?
}