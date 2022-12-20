-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: book_store
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `author_book`
--

use heroku_2b4bd5145fca32f;

DROP TABLE IF EXISTS `author_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `author_book` (
  `book_id` bigint NOT NULL,
  `author_id` bigint NOT NULL,
  KEY `FK7cqs8nb7l859jcwwqoawcokqf` (`author_id`),
  KEY `FKmeehr164a2cpxegeiawuv40a3` (`book_id`),
  CONSTRAINT `FK7cqs8nb7l859jcwwqoawcokqf` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`),
  CONSTRAINT `FKmeehr164a2cpxegeiawuv40a3` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author_book`
--

LOCK TABLES `author_book` WRITE;
/*!40000 ALTER TABLE `author_book` DISABLE KEYS */;
INSERT INTO `author_book` VALUES (9,66),(10,67),(11,69),(11,70),(12,71),(13,72),(13,73),(13,74),(14,75),(15,75),(17,76),(18,77),(19,78),(19,79),(16,76),(20,80),(21,81),(22,82),(23,83),(24,84),(25,85),(25,86),(26,87),(27,88),(27,89),(28,90);
/*!40000 ALTER TABLE `author_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `authors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci,
  `fullname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (66,'Là một nhà văn, nhà thơ, bình luận viên Việt Nam. Ông được biết đến qua nhiều tác phẩm văn học về đề tài tuổi trẻ, các tác phẩm của ông rất được độc giả ưa chuộng và nhiều tác phẩm đã được chuyển thể thành phim.','Nguyễn Nhật Ánh'),(67,'','Yosbook'),(69,'','Mộc An'),(70,'','Phan Hiền'),(71,'','Valentin Verthé'),(72,'','Komorebi'),(73,'','Vũ Linh'),(74,'','Quyên Thái'),(75,'','Trần Đình Chất'),(76,'','Jeffrey Archer'),(77,'Phan Văn Trường là chuyên gia cao cấp trong lĩnh vực đàm phán quốc tế và là cố vấn của Chính phủ Pháp về thương mại Quốc tế.[1] Ông được Tổng thống Pháp trao tặng Huy Chương Hiệp Sĩ Bắc Đẩu Bội Tinh (Chevalier de la Légion d’Honneur) năm 2007.','Phan Văn Trường'),(78,'','Robert T Kiyosaki'),(79,'','John Fleming'),(80,'Song Hong Binh (Tống Hồng Binh) sinh năm 1968 tại Tứ Xuyên, Trung Quốc. Ông là tác giả của rất nhiều cuốn sách bán chạy, là học giả nghiên cứu tài chính thế giới và là Viện trưởng Viện nghiên cứu Kinh tế Tài chính Hoàn Cầu, Bắc Kinh.','Song Hong Binh'),(81,'','Samuel Bostaph'),(82,'','Park Han Seul'),(83,'','Viện Đông Y'),(84,'Bác sĩ Ray D. Strand tốt nghiệp Đại học Y khoa Colorado và hoàn thành khóa đào tạo sau đại học tại bệnh viện Mercy ở San Diego, California. Là bác sĩ gia đình trong ba mươi năm, ông đã có kinh nghiệm thực hành và giảng dạy về y học dinh dưỡng trong bảy năm. Ông sống trong một trang trại ngựa ở Nam Dakota với người vợ đáng yêu Elizabeth và ba người con.','BS Ray D Strand'),(85,'','Nguyễn Tuấn Anh'),(86,'','Cao Minh Quang'),(87,'','Gottlob Frege'),(88,'','Mai Lan Hương'),(89,'','Hà Thanh Uyên'),(90,'','Trang Anh');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount_in_stock` bigint DEFAULT NULL,
  `book_title` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci,
  `entry_price` double DEFAULT NULL,
  `img_link` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `publishing_year` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKleqa3hhc0uhfvurq6mil47xk0` (`category_id`),
  CONSTRAINT `FKleqa3hhc0uhfvurq6mil47xk0` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (9,1000,'Những Người Hàng Xóm','Câu chuyện đi theo lời kể của một anh chàng mới lấy vợ, chuẩn bị đi làm và có ý thích viết văn. Anh chàng yêu vợ theo cách của mình, khen ngợi sùng bái người yêu cũng theo cách của mình, nhưng nhìn cuộc đời theo cách sống của những người hàng xóm. Sống trong tình yêu của vợ đầy mùi thơm và nhiều vị ngọt. Chứng kiến tình yêu của anh cảnh sát với cô bạn gái ngành y; mối tình thứ hai của người phụ nữ tốt bụng phát thanh viên ngôn ngữ ký hiệu. Và được chiêm nghiệm trong tình yêu đắm đuối mỗi ngày của ông họa sĩ già thương nhớ người vợ xinh đẹp-người mẫu, nàng thơ của ông.',90000,'https://www.netabooks.vn/Data/Sites/1/Product/50436/1nhung-nguoi-hang-xom-ban-dac-biet-3.jpg',85000,2022,20),(10,1000,'365 Câu Chuyện Trí Tuệ','Kho tri thức về tự nhiên , xã hội của con người ngày nay là một đại dương bao la . Nhưng những gì mà con người chưa khám phá ra còn nhiều hơn gấp ngàn lần những điều ta biết . Cho dù chúng ta học trong nhà trường và ngoài xã hội có nhiều đến đâu thì điều ta biết vẫn vô cùng bé nhỏ so với biển trời kiến thức mà nhân loại đã có được và chưa có được . Các bạn nhỏ hãy bổ sung vào kho kiến thức của mình những tri thức  về thế giới tự nhiên qua cuốn Bách khoa thiếu nhi – 365 câu chuyện trí tuệ - Hỏi đáp kiến thức thiên nhiên kỳ diệu.',90000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935236429061.jpg',83300,2022,21),(11,1000,'Vần Điệu Cho Em - Cây Cầu Lấp Lánh','Thế giới của trong tập thơ “Cây cầu lấp lánh” cũng lấp la lấp lánh, nhiều niềm vui và đầy sự tưởng tượng của trẻ em. Mỗi bài thơ là một lời thầm thì với những trao gửi, yêu thương từ mẹ với con, từ con với mẹ, từ con với cỏ cây hoa lá, từ cây lá, cỏ hoa đến con, và cái nhìn trong vắt trong veo của con đến với cuộc đời. Mọi thứ, đều là những \"mật ngữ\" đầy kỳ diệu, đầy cảm xúc tích cực, và tác giả muốn trao gửi cảm xúc này đến mọi trẻ em. Những bài thơ nhỏ, nếu được ba mẹ cùng đọc với con, hoặc con có thể tự đọc, sẽ giúp trẻ yêu thích việc học và ghi nhớ ngôn ngữ cũng như tương tác nhiều hơn với thế giới bên ngoài.',25000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8934974183686.jpg',32500,2020,21),(12,996,'Hỏi Đáp Cùng Em! - Bóng Đá','Một cuốn sách giải đáp hơn 200 câu hỏi về những cầu thủ, đội bóng nổi tiếng cùng những giải bóng đá và kỷ lục hấp dẫn nhất.',170000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935235236141.jpg',210000,2021,21),(13,1000,'A! Tết Là Đây!','Một cuốn sách Tết “động đậy” nhất từ trước đến nay dành cho các bé! Tương tác - Tưởng tượng – Thích thú cùng những trang sách đầy bất ngờ, cho bé trải nghiệm những hoạt động và trò chơi nhằm phát triển khả năng sắp xếp, phân loại, tư duy logic và sáng tạo.',65000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244881516.jpg',74000,2022,21),(14,1500,'Vui Buồn Hoa Phượng','Trong vòng hơn sáu mươi năm, từ lúc còn học phổ thông, cho đến thời sinh viên, rồi những năm tháng đứng trên bục giảng của trường đại học, chứng kiến biết bao thăng trầm của ngành giáo dục, không thể thờ ơ với mọi chuyện buồn vui, tác giả đã ghi lại vài chuyện trong đó qua cuốn sách Vui buồn hoa phượng.\r\n\r\nNhững câu chuyện được viết trong cuốn sách này hoàn toàn căn cứ vào “người thật việc thật”. Tác giả không có ý định khen hay chê ai vì điều đó thật là vô nghĩa, chỉ mong mọi người nghĩ tới một điều gì đó lớn lao hơn, nhân văn hơn.',130000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935280912892.jpg',175000,2020,22),(15,100,'Chuyện Người, Chuyện Ta','Thật bất ngờ khi nhận được cuốn sách! Nhưng đây là một bất ngờ rất thú vị!\r\n\r\nTé ra, bạn tôi, một cựu học sinh Lớp Chuyên Văn Nghệ An cách đây đúng 50 năm, vẫn không phai nhạt “máu văn chương” vốn được ươm mầm, nuôi dưỡng trong những điều kiện “đặc biệt” nhưng đầy gian khó của những năm chiến tranh ác liệt chống Mỹ cứu nước. Lớp Chuyên Văn của chúng tôi hồi đó, lúc nhập học có 36 học sinh, nhưng đến khi ra trường chỉ còn 27 người vì nhiều lý do khác nhau, trong đó có một số bạn đã lên đường nhập ngũ. Tuy là học sinh chuyên Văn nhưng có không ít bạn lại học rất giỏi các môn Toán, Lý, Hoá, trong đó có Trần Đình Chất, nên đã được tham gia dự tuyển vào Đại học quân sự nước ngoài do Bộ Quốc phòng tuyển chọn, theo đó phải thi Khối A. Tuy nhiên, cuối cùng vẫn thi Khối C như đã đăng ký trước đó và đạt điểm cao, được chọn đi học nước ngoài. Nhưng tại đây lại được chuyển sang học chuyên ngành Kinh tế Thuỷ sản.\r\n\r\nTôi cứ ngỡ, các phương trình, phép tính, công trình nghiên cứu khoa học đã đè bẹp cảm hứng văn chương của Trần Đình Chất rồi, nào ngờ…\r\nThú thật, tôi đọc tập Chuyện người, chuyện ta với không ít tò mò. Bạn tôi viết gì đây?\r\n\r\nĐúng là có cả chuyện người và chuyện ta. Người ở đây là thiên hạ, là nước này nước nọ, là đất nước Ba Lan yêu quý mà Trần Đình Chất đã du học thời trai trẻ và sau này trở lại làm luận án tiến sĩ. Ta ở đây là đất nước mình, làng quê mình, là nơi mình làm việc, sinh sống và chính bản thân mình.\r\n\r\nCuốn sách được thể hiện bằng một lối viết khá đa dạng, lúc thì kiểu hồi ký đầy chất hoài niệm (Quả đất tròn, Giáo sư hướng dẫn tôi , Anh bạn người Ba Lan của tôi, Chuyện ông chủ nhà trọ của tôi), lúc thì bằng lối văn trần thuật với sự miêu tả sống động (Chuyên về phố đèn đỏ, Chuyện ghi trên đường tập thể dục, Nghĩa trang Trường Sơn, Bạn tôi muốn làm giàu, Bạn tôi muốn thằng hàng xóm cũng giàu, Chuyện của nàng Thiên Hương, Lấy con gái của kẻ thù, Chuyện tình của bạn tôi). Tôi thích loạt bài viết đầy chất suy tư, chiêm nghiệm, ý tứ sắc lẹm với sự so sánh, phân tích, mổ xẻ khá thấu đáo (Chuyện học, Thần đồng, Giải Nobel, Cống hiến, Đại ngôn, Khôn vặt, Ăn mày, Kẻ thù, Người lãnh đạo). Tôi cảm nhận đây là những bài viết vừa thể hiện vốn sống, sự trải nghiệm của tác giả như một công dân toàn cầu, vừa cho thấy khả năng khái quát, tổng hợp tri thức phong phú và cách luận giải của một nhà khoa học. Cái độc đáo của loạt bài viết này là các tít bài chỉ có hai chữ, rất súc tích, nhưng tính luận đề rất cao. Trong Thần đồng, Giải Nobel và Cống hiến, người đọc được tiếp cận một hệ thống tư liệu khá đầy đủ và tin cậy cả trong nước và quốc tế về ba chủ đề này. Đọc thấy vui và bổ ích.\r\n\r\nNhà báo, nhà văn Hồ Quang Lợi\r\nNguyên Phó Chủ tịch Thường trực Hội Nhà báo Việt Nam\r\n\r\nMã hàng	8935280910133\r\nTên Nhà Cung Cấp	Thái Hà\r\nTác giả	Trần Đình Chất\r\nNXB	NXB Hà Nội\r\nNăm XB	2022\r\nTrọng lượng (gr)	450\r\nKích Thước Bao Bì	24 x 16 x 2 cm\r\nSố trang	412\r\nHình thức	Bìa Mềm\r\nSản phẩm hiển thị trong	\r\nThái Hà\r\nSản phẩm bán chạy nhất	Top 100 sản phẩm Truyện ngắn - Tản Văn bán chạy của tháng\r\nGiá sản phẩm trên Fahasa.com đã bao gồm thuế theo luật hiện hành. Bên cạnh đó, tuỳ vào loại sản phẩm, hình thức và địa chỉ giao hàng mà có thể phát sinh thêm chi phí khác như Phụ phí đóng gói, phí vận chuyển, phụ phí hàng cồng kềnh,...\r\nChuyện Người Chuyện Ta\r\n\r\nThật bất ngờ khi nhận được cuốn sách! Nhưng đây là một bất ngờ rất thú vị!\r\n\r\nTé ra, bạn tôi, một cựu học sinh Lớp Chuyên Văn Nghệ An cách đây đúng 50 năm, vẫn không phai nhạt “máu văn chương” vốn được ươm mầm, nuôi dưỡng trong những điều kiện “đặc biệt” nhưng đầy gian khó của những năm chiến tranh ác liệt chống Mỹ cứu nước. Lớp Chuyên Văn của chúng tôi hồi đó, lúc nhập học có 36 học sinh, nhưng đến khi ra trường chỉ còn 27 người vì nhiều lý do khác nhau, trong đó có một số bạn đã lên đường nhập ngũ. Tuy là học sinh chuyên Văn nhưng có không ít bạn lại học rất giỏi các môn Toán, Lý, Hoá, trong đó có Trần Đình Chất, nên đã được tham gia dự tuyển vào Đại học quân sự nước ngoài do Bộ Quốc phòng tuyển chọn, theo đó phải thi Khối A. Tuy nhiên, cuối cùng vẫn thi Khối C như đã đăng ký trước đó và đạt điểm cao, được chọn đi học nước ngoài. Nhưng tại đây lại được chuyển sang học chuyên ngành Kinh tế Thuỷ sản.',130000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935280910133.jpg',158000,2021,22),(16,100,' Hai Số Phận','“Hai số phận” không chỉ đơn thuần là một cuốn tiểu thuyết, đây có thể xem là \"thánh kinh\" cho những người đọc và suy ngẫm, những ai không dễ dãi, không chấp nhận lối mòn.\r\n“Hai số phận” làm rung động mọi trái tim quả cảm, nó có thể làm thay đổi cả cuộc đời bạn. Đọc cuốn sách này, bạn sẽ bị chi phối bởi cá tính của hai nhân vật chính, hoặc bạn là Kane, hoặc sẽ là Abel, không thể nào nhầm lẫn. Và điều đó sẽ khiến bạn thấy được chính mình.',80000,'https://salt.tikicdn.com/cache/750x750/ts/product/c7/35/be/e5bf89f34cbe25d9e475708e69483f17.jpg.webp',96000,2020,22),(17,100,'Tell Tale','Find out what happens to the hapless young detective from Naples who travels to an Italian hillside town to solve a murder and the pretentious schoolboy whose discovery of the origins of his father’s wealth changes his life forever. Follow the stories of the woman who dares to challenge the men at her Ivy League university during the 1930s, and another young woman who thumbs a lift and has an encounter of a lifetime.',60000,'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_19231_thanh_ly.jpg',71000,2018,22),(18,100,'Công Dân Toàn Cầu - Công Dân Vũ Trụ','Những \"công dân toàn cầu\" mang những nét đặc trưng nào? Họ có sinh hoạt, làm việc, và hành xử theo các chuẩn mực khác biệt của riêng một cộng đồng mang đẳng cấp cao? Liệu có những hình mẫu nào để chúng ta tham khảo, từ đó tự rèn cho mình phong thái của một \"công dân toàn cầu\"? Và xa hơn nữa, một công dân của Vũ trụ? Bằng cách mô tả vừa rộng vừa sâu, vừa bao quát nhưng vẫn cung cấp nhiều ví dụ cụ thể, tác giả Phan Văn Trường mang đến một cách nhìn mới mẻ và sâu sắc về hình ảnh của những con người yêu thương đồng loại, trách nhiệm với địa cầu, và trân quý Vũ trụ mà chúng ta đang sống.',60000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8934974179221.jpg',76000,2022,23),(19,100,'Doanh Nghiệp Của Thế Kỷ 21','Một quyển sách khác của tác giả bộ sách nổi tiếng Dạy con làm giàu. Trong cuốn sách này, Robert T. Kiyosaki sẽ chỉ ra cho bạn đọc thấy lý do tại sao mình cần phải gây dựng doanh nghiệp riêng của mình và chính xác đó là doanh nghiệp gì. Nhưng đây không phải là việc thay đổi loại hình doanh nghiệp mình đang triển khai mà đó là việc thay đổi chính bản thân. Tác giả còn cho bạn đọc biết cách thức tìm kiếm những gì mình cần để phát triển doanh nghiệp hoàn hảo, nhưng để doanh nghiệp của mình phát triển thì mình cũng sẽ phải phát triển theo.',65000,'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_603.jpg',72000,2019,23),(20,100,'Chiến Tranh Tiền Tệ ','Sau hai cuốn đầu tiên lần lượt diễn giải lịch sử phát triển tài chính của Hoa Kỳ và châu Âu, tác giả đặt mục tiêu cho phần 3 vào Trung Quốc, bắt đầu từ Chiến tranh nha phiến, tìm hiểu và giải mã sự phát triển tài chính của đất nước này.\r\n\r\nLịch sử gần 100 năm của Trung Quốc, từ góc độ tài chính cho thấy, bất cứ ai có thể kiểm soát biên giới tài chính đều có lợi thế chiến lược rất lớn, có thể thao túng và chi phối rất nhiều mặt trong xã hội. Nên sự sụp đổ của biên giới tài chính cuối cùng sẽ dẫn đến sự sụp đổ của một chế độ, nhà nước bất kì.\r\n\r\nNắm được biên giới tài chính, sức mạnh tấn công của Anh với Trung Quốc trở nên mạnh hơn nhiều. Họ đánh bại tiêu chuẩn tiền tệ của Trung Quốc, nắm giữ đỉnh cao của chiến lược tài chính ngân hàng trung ương, thâm nhập và làm xói mòn hệ thống tài chính, kiểm soát thị trường và tước đi quyền lực của nhà Thanh trong rất nhiều mặt.',90000,'https://cdn0.fahasa.com/media/catalog/product/i/m/image_200687.jpg',108000,2021,23),(21,100,'Andrew Carnegie - Từ Cậu Bé Nghèo Trở Thành Ông Vua Thép Của Nền Công Nghiệp Mỹ','Andrew Carnegie không chỉ là một nhà đại tư bản của nước Mỹ, mà còn là một trong những người bác ái, đóng góp tài sản cho sự phát triển của cộng đồng và nhân loại, một trong những doanh nhân lừng danh bậc nhất thế kỷ 19.\r\n\r\nTrong cuốn sách tiểu sử về Andrew Carnegie, tác giả Samuel Bostaph tập trung phân tích sự nghiệp của Carnegie thông qua các góc nhìn kinh tế, và tầm ảnh hưởng ông tạo nên đối với giới kinh doanh cũng như chính trị - xã hội Hoa Kỳ trong giai đoạn chuyển mình đầy biến động của đất nước cờ hoa.',90000,'https://cdn0.fahasa.com/media/catalog/product/i/m/image_180259.jpg',120000,2017,23),(22,100,' Hiểu Cơ Thể Khi Uống Thuốc','Trong thời đại mà thuốc là vật phẩm hỗ trợ sức khỏe vô cùng phổ biến, ai ai cũng uống một loại thuốc nào đó, thì việc uống thuốc đúng cách trở nên rất quan trọng. Với những người uống thuốc mà bỏ qua bước tìm hiểu, hoặc tùy tiện thiết kế quy trình uống thuốc cho bản thân mà không qua tham vấn, thuốc có thể chẳng mang lại lợi ích gì cả. Dù là thuốc trị tiểu đường hay thuốc giảm đau, thuốc chống dị ứng hay thuốc kháng virus, mọi loại thuốc đều có những cơ chế và quy tắc riêng mà ta cần hiểu và tuân thủ để thuốc phát huy được tác dụng tốt nhất. ',100000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935235236165.jpg',137000,2022,24),(23,100,'Phương Pháp Bào Chế Và Sử Dụng Đông Dược','',130000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935236429122.jpg',153000,2021,24),(24,98,'Y Học Dinh Dưỡng - Những Điều Bác Sĩ Không Nói Với Bạn','Tuy là một bác sĩ nhiều năm kinh nghiệm, nhưng bác sĩ Ray Strand đã phải bó tay chứng kiến người vợ bị những cơn đau mãn tính hành hạ. Từ đó ông đã đồng ý thử chế độ bổ sung dinh dưỡng mà một người hàng xóm đề nghị. Thật ngạc nhiên, tình trạng của vợ ông bắt đầu cải thiện gần như ngay lập tức. Bước ngoặt đáng kinh ngạc đó đã khiến ông cống hiến hết mình để nghiên cứu các liệu pháp thay thế trong y học, đặc biệt là trong lĩnh vực bổ sung dinh dưỡng.\r\nNhững kiến thức mà bác sĩ Strand đưa ra về kẻ thù thầm lặng của cơ thể - sự căng thẳng oxy hóa - sẽ làm bạn kinh ngạc. Nhưng, quan trọng hơn, nghiên cứu của ông sẽ trang bị cho bạn kiến thức dinh dưỡng để bảo vệ hoặc lấy lại sức khỏe của mình, đẩy lùi cũng như ngăn ngừa bệnh tật.',130000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935270703936.jpg',157000,2022,24),(25,100,'Bất Đẳng Thức Dưới Góc Nhìn Của Các Bổ Đề','Mỗi bài toán có một vai trò riêng của nó. Có bài được đề ra để kiểm tra khả năng tư duy, suy luận, có bài sẽ kiểm tra khả năng ghi nhớ công thức, một số bài là để kiểm tra kỹ năng tính toán và cũng có bài thách thức người học để bứt phá bản thân. Vậy nên, để việc học toán trở nên nhẹ nhàng, thú vị hơn, hãy đặt mỗi bài toán vào đúng vị trí, vai trò của nó và sự sáng tạo cũng nảy mầm từ đây. Bất đẳng thức (BĐT) cũng chỉ cần có vậy!',170000,'https://cdn0.fahasa.com/media/catalog/product/8/9/8935217113996.jpg',212000,2021,25),(26,100,' Các Cơ Sở Của Số Học','Vào năm 1935, Alfred Frege đã trao phần di cảo – Nachlass của Frege cho Heinrich Scholz thuộc Đại học Munchen, người trước đó đã tìm kiếm chúng, Scholz và những cộng sự đã bắt tay vào biên tập chúng để xuất bản. Trong thế chiến II, phần bản gốc được giữ ở University Library cho an toàn, tuy nhiên chúng đã bị bom của phe đồng minh phá hủy vào ngày 25 tháng 03 năm 1945. Mặc dù may mắn là các bản sao đã được thực hiện đối với những gì được cho là quan trọng, Scholz đã sao chép chúng một lần nữa sau thế chiến II để chuẩn bị xuất bản. Nhưng bệnh tật của Scholz khiến cho ông không thể hoàn thành dự án này trước khi qua đời vào năm 1956; mãi đến năm 1969 thì Nachgelassene Schriften mới được xuất bản, được biên tập bởi một nhóm học giả do Hans Hemes lập nên, người kế nhiệm Schloz ở Munchen, và sau đó là một tập thư từ được công bố vào năm 1976. Hơn 50 năm sau khi Frege qua đời, những gì là vàng còn lại mà Frege đã nhắc đến mới xuất hiện, và sự tiên tri của Frege rằng một ngày nào đó nó sẽ được đánh giá cao hơn nhiều so với lúc ông viết chúng đã trở thành hiện thực.',110000,'https://cdn0.fahasa.com/media/catalog/product/9/7/9786048463915.jpg',144000,2021,25),(27,100,' Giải Thích Ngữ Pháp Tiếng Anh ','Ngữ pháp Tiếng Anh tổng hợp các chủ điểm ngữ pháp trọng yếu mà học sinh cần nắm vững. Các chủ điểm ngữ pháp được trình bày rõ ràng, chi tiết. Sau mỗi chủ điểm ngữ pháp là phần bài tập & đáp án nhằm giúp các em củng cố kiến thức đã học, đồng thời tự kiểm tra kết quả.',100000,'https://cdn0.fahasa.com/media/catalog/product/z/3/z3097453775918_7ea22457f168a4de92d0ba8178a2257b.jpg',134000,2021,26),(28,100,'Cẩm Nang Cấu Trúc Tiếng Anh','Cuốn sách CẨM NANG CẤU TRÚC TIẾNG ANH gồm 25 phần, mỗi phần là một phạm trù kiến thức trong tiếng Anh được trình bày một cách ngắn gọn, đơn giản, cô đọng và hệ thống hoá dưới dạng sơ đồ, bảng biểu nhằm phát triển khả năng tư duy của người học và từ đó giúp người học nhớ kiến thức nhanh hơn và sâu hơn.\r\n\r\nSau hầu hết các phần lí thuyết đều có 20-30 câu bài tập áp dụng để kiểm tra cũng như khắc sâu kiến thức cho người học.\r\n\r\nTuy dày chưa đến 250 trang nhưng cuốn sách lại có thể bao trọn toàn bộ kiến thức từ đơn giản đến phức tạp cộng với cách tận dụng tối đa và áp dụng triệt để cách học tiếng Anh bằng sơ đồ tư duy.\r\n',50000,'https://cdn0.fahasa.com/media/catalog/product/i/m/image_187866.jpg',65000,2021,26);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (20,'Tiểu thuyết'),(21,'Thiếu Nhi'),(22,'Văn học'),(23,'Kinh tế'),(24,'Y học'),(25,'Toán học'),(26,'Ngoại ngữ');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `order_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `price` double DEFAULT NULL,
  `book_id` bigint DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjqe04yonp6a52rhbf2y0m03qw` (`book_id`),
  KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
  CONSTRAINT `FKjqe04yonp6a52rhbf2y0m03qw` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (4,8,210000,12,2),(5,1,32500,11,2),(6,4,96000,16,4),(7,2,153000,23,4),(8,2,158000,15,4),(9,4,153000,23,5),(10,2,157000,24,6),(11,4,210000,12,6),(12,1,175000,14,7);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` datetime DEFAULT NULL,
  `order_status` int DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (2,'2022-12-18 20:10:27',2,8),(4,'2022-12-19 18:18:12',2,8),(5,'2022-12-19 23:14:24',0,6),(6,'2022-12-19 23:27:51',2,10),(7,'2022-12-19 23:32:45',0,10);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile_user`
--

DROP TABLE IF EXISTS `profile_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `profile_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `phone_number` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs3sunokbxcofhx27qv03rha3g` (`user_id`),
  CONSTRAINT `FKs3sunokbxcofhx27qv03rha3g` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile_user`
--

LOCK TABLES `profile_user` WRITE;
/*!40000 ALTER TABLE `profile_user` DISABLE KEYS */;
INSERT INTO `profile_user` VALUES (2,'tb9','2022-12-09','Nam','0333273003',8),(3,'tb 3','2022-12-08','Nam','0333273003',10);
/*!40000 ALTER TABLE `profile_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registration_token`
--

DROP TABLE IF EXISTS `registration_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `registration_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiration` bigint DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpmf52fk27bywm8is5s2jv3xam` (`user_id`),
  CONSTRAINT `FKpmf52fk27bywm8is5s2jv3xam` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registration_token`
--

LOCK TABLES `registration_token` WRITE;
/*!40000 ALTER TABLE `registration_token` DISABLE KEYS */;
INSERT INTO `registration_token` VALUES (6,60,'2022-12-18 18:07:45','fc16a2a8-fa81-4d44-9b4b-84cdb01cb27c',6),(8,60,'2022-12-18 18:18:32','1078b9c9-b703-4333-b912-39eeb9ce2a0c',8),(10,60,'2022-12-19 23:26:24','6e12d772-4710-4eed-a936-4e6ea77b34e8',10);
/*!40000 ALTER TABLE `registration_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reset_password_token`
--

DROP TABLE IF EXISTS `reset_password_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `reset_password_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiration` bigint DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlu8wgvb3mkbxir0sls596u7np` (`user_id`),
  CONSTRAINT `FKlu8wgvb3mkbxir0sls596u7np` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reset_password_token`
--

LOCK TABLES `reset_password_token` WRITE;
/*!40000 ALTER TABLE `reset_password_token` DISABLE KEYS */;
INSERT INTO `reset_password_token` VALUES (2,60,'2022-12-20 00:28:35','48d5b8f7-0fc0-4777-b81e-98e03fde3fb6',10);
/*!40000 ALTER TABLE `reset_password_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'USER'),(2,'ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `FKt7e7djp752sqn6w22i6ocqy6q` (`role_id`),
  KEY `FKj345gk1bovqvfame88rcx7yyx` (`user_id`),
  CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt7e7djp752sqn6w22i6ocqy6q` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (6,1),(6,2),(8,1),(8,2),(10,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `fullname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `username` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (6,'Taikhoan26s19@gmail.com',_binary '','nam 1','$2a$10$bN8gzjP0CPmxJJoF0JC3dOQy1.rHZ.ZNaSAYxTo/ZR5zuUg78fDrG','nam1'),(8,'Taikhoadan2619@gmail.com',_binary '','nam 2','$2a$10$exODpBxU3ywQWFzzoD39zOKks7/uzj3JyUtzQBezJUk6bq1Z248rO','nam2'),(10,'taikhoan2619@gmail.com',_binary '\0','nam 3','$2a$10$GO1A8urPuAD6w4eV.78Esu4BuOKRnPNZzFvErX6xuv3bu3FHxKNfe','nam3');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-20 16:36:05
