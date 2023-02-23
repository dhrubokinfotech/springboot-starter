package com.disl.starter.constants;

public final class AppTables {
	private AppTables() {}

	public static final String rolePrivilege =  "ROLE_PRIVILEGE";
	public static final String userRole = "USER_ROLE";

	public static final String user = "USER";
	public static final String role = "ROLE";
	public static final String pageName = "page";
	public static final String dbFileName = "db_file";
	public static final String sectionName = "section";
	public static final String privilege = "PRIVILEGE";
	public static final String notificationName = "notification";

	public static final class UserTable {
		public static final String id = "ID";
		public static final String email = "EMAIL";
		public static final String name = "name";
		public static final String banned = "banned";
		public static final String verified = "verified";
		public static final String password = "PASSWORD";
		public static final String passwordResetToken = "PASSWORD_RESET_TOKEN";
	}

	public static final class RoleTable {
		public static final String roleId = "ID";
		public static final String roleType = "ROLE_TYPE";
		public static final String description = "description";
		public static final String imageUrl = "image_url";
		public static final String roleName = "ROLE_NAME";
	}

	public static final class PrivilegeTable {
		public static final String id = "ID";
		public static final String name = "NAME";
		public static final String descName = "DESC_NAME";
	}

	public static final class PageTable {
		private PageTable() {}

		public static final String id = "id";
		public static final String tag = "tag";
		public static final String title = "title";
		public static final String description = "description";
	}

	public static final class SectionTable {
		private SectionTable() {}

		public static final String id = "id";
		public static final String title = "title";
		public static final String active = "active";
		public static final String pageId = "page_id";
		public static final String content = "content";
		public static final String description = "description";
		public static final String imageFileId = "image_file_id";
		public static final String externalLink = "external_link";
	}

	public static final class DbFileTable {
		private DbFileTable() {}

		public static final String id = "id";
		public static final String fileKey = "file_key";
		public static final String fileName = "file_name";
		public static final String mimeType = "mime_type";
		public static final String fileType = "file_type";
		public static final String uploadType  = "upload_type";
		public static final String fileExtension  = "file_extension";
	}

	public static final class NotificationTable {
		private NotificationTable() {}

		public static final String id = "id";
		public static final String type = "type";
		public static final String isRead = "is_read";
		public static final String typeID = "type_id";
		public static final String title = "title";
		public static final String message = "message";
		public static final String senderId = "sender_id";
		public static final String recipientId = "recipient_id";
	}
}