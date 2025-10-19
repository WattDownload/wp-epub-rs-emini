use uniffi::deps::anyhow;
use wp_mini_epub::AppError;

#[derive(Debug, thiserror::Error, uniffi::Error)]
pub enum WPEpubError {
    #[error("Authentication failed")]
    AuthenticationFailed,
    #[error("Not logged in")]
    NotLoggedIn,
    #[error("Logout failed")]
    LogoutFailed,
    #[error("Story not found")]
    StoryNotFound(u64),
    #[error("Metadata fetch failed")]
    MetadataFetchFailed,
    #[error("Download failed")]
    DownloadFailed,
    #[error("Chapter processing failed")]
    ChapterProcessingFailed,
    #[error("EPUB generation failed")]
    EpubGenerationFailed,
    #[error("IO error: {0}")]
    IoError(String),
    #[error("Unknown error")]
    Unknown,
}

impl From<anyhow::Error> for WPEpubError {
    fn from(err: anyhow::Error) -> Self {
        if let Some(app_error) = err.downcast_ref::<AppError>() {
            return match app_error {
                AppError::AuthenticationFailed => Self::AuthenticationFailed,
                AppError::NotLoggedIn => Self::NotLoggedIn,
                AppError::LogoutFailed => Self::LogoutFailed,
                AppError::StoryNotFound(id) => Self::StoryNotFound(*id as u64),
                AppError::MetadataFetchFailed => Self::MetadataFetchFailed,
                AppError::DownloadFailed => Self::DownloadFailed,
                AppError::ChapterProcessingFailed => Self::ChapterProcessingFailed,
                AppError::EpubGenerationFailed => Self::EpubGenerationFailed,
                AppError::IoError(e) => Self::IoError(e.to_string()),
            };
        }

        // Fallback for generic errors
        WPEpubError::IoError(err.to_string())
    }
}

impl From<AppError> for WPEpubError {
    fn from(err: AppError) -> Self {
        match err {
            AppError::AuthenticationFailed => Self::AuthenticationFailed,
            AppError::NotLoggedIn => Self::NotLoggedIn,
            AppError::LogoutFailed => Self::LogoutFailed,
            AppError::StoryNotFound(id) => Self::StoryNotFound(id as u64),
            AppError::MetadataFetchFailed => Self::MetadataFetchFailed,
            AppError::DownloadFailed => Self::DownloadFailed,
            AppError::ChapterProcessingFailed => Self::ChapterProcessingFailed,
            AppError::EpubGenerationFailed => Self::EpubGenerationFailed,
            AppError::IoError(e) => Self::IoError(e.to_string()),
        }
    }
}
