INSERT INTO karma.brand_car (id_car, name) VALUES
(1, 'Acura'),
(2, 'Alfa Romeo'),
(3, 'Aston Martin'),
(4, 'Audi'),
(5, 'Bentley'),
(6, 'BMW'),
(7, 'Buick'),
(8, 'Cadillac'),
(9, 'Chery'),
(10, 'Chevrolet'),
(11, 'Chrysler'),
(12, 'Citroen'),
(13, 'Daewoo'),
(14, 'Daihatsu'),
(15, 'Dodge'),
(16, 'FAW'),
(17, 'Ferrari'),
(18, 'Fiat'),
(19, 'Ford'),
(20, 'Geely'),
(21, 'GMC'),
(22, 'Great Wall'),
(23, 'Haima'),
(24, 'Honda'),
(25, 'Hummer'),
(26, 'Hyundai'),
(27, 'Infiniti'),
(28, 'Isuzu'),
(29, 'Iveco'),
(30, 'Jaguar'),
(31, 'Jeep'),
(32, 'Kia'),
(33, 'Lancia'),
(34, 'Land Rover'),
(35, 'Lexus'),
(36, 'Lifan'),
(37, 'Lincoln'),
(38, 'Lotus'),
(39, 'Maserati'),
(40, 'Maybach'),
(41, 'Mazda'),
(42, 'Mercedes'),
(43, 'Mercury'),
(44, 'MG'),
(45, 'Mini'),
(46, 'Mitsubishi'),
(47, 'Nissan'),
(48, 'Opel'),
(49, 'Peugeot'),
(50, 'Pontiac'),
(51, 'Porsche'),
(52, 'Renault'),
(53, 'Rolls Royce'),
(54, 'Rover'),
(55, 'Saab'),
(56, 'Saturn'),
(57, 'Scion'),
(58, 'Seat'),
(59, 'Skoda'),
(60, 'Smart'),
(61, 'Ssang Yong'),
(62, 'Subaru'),
(63, 'Suzuki'),
(64, 'Toyota'),
(65, 'Volkswagen'),
(66, 'Volvo'),
(67, 'ZAZ'),
(68, 'ГАЗ'),
(69, 'ВАЗ'),
(70, 'ТаГАЗ'),
(71, 'УАЗ');


INSERT INTO karma.model_car (id_model, brand_id_int, name) VALUES
(1, 1, 'CL'),
(2, 1, 'EL'),
(3, 1, 'Integra'),
(4, 1, 'MDX'),
(5, 1, 'NSX'),
(6, 1, 'RDX'),
(7, 1, 'RL'),
(8, 1, 'RSX'),
(9, 1, 'TL'),
(10, 1, 'TSX'),
(11, 1, 'ZDX'),
(12, 2, '146'),
(13, 2, '147'),
(14, 2, '147 GTA'),
(15, 2, '156'),
(16, 2, '156 GTA'),
(17, 2, '159'),
(18, 2, '166'),
(19, 2, '8C'),
(20, 2, 'Brera'),
(21, 2, 'Giulietta'),
(22, 2, 'GT'),
(23, 2, 'GTV'),
(24, 2, 'MiTo'),
(25, 2, 'Spider'),
(26, 3, 'DB9'),
(27, 3, 'DBS'),
(28, 3, 'DBS Volante'),
(29, 3, 'Rapide'),
(30, 3, 'V12 Vantage'),
(31, 3, 'V8 Vantage'),
(32, 3, 'Vanquish S'),
(33, 4, 'A1'),
(34, 4, 'A2'),
(35, 4, 'A3'),
(36, 4, 'A4'),
(37, 4, 'A4 Allroad quattro'),
(38, 4, 'A5'),
(39, 4, 'A6'),
(40, 4, 'A7'),
(41, 4, 'A8'),
(42, 4, 'Allroad'),
(43, 4, 'Q5'),
(44, 4, 'Q7'),
(45, 4, 'R8'),
(46, 4, 'R8 V10'),
(47, 4, 'RS4'),
(48, 4, 'RS6'),
(49, 4, 'S4'),
(50, 4, 'S5'),
(51, 4, 'S6'),
(52, 4, 'S8'),
(53, 4, 'TT'),
(54, 4, 'TT RS'),
(55, 4, 'TT S'),
(56, 5, 'Arnage'),
(57, 5, 'Azure'),
(58, 5, 'Brooklands'),
(59, 5, 'Continental'),
(60, 5, 'Mulsanne'),
(61, 6, '1-series (E87)'),
(62, 6, '3-series (E46)'),
(63, 6, '3-series (E90)'),
(64, 6, '5-series (E39)'),
(65, 6, '5-series (E60)'),
(66, 6, '5-series (F10)'),
(67, 6, '6-series (E63)'),
(68, 6, '7-series (E38)'),
(69, 6, '7-series (E65,E66)'),
(70, 6, '7-series (F01,F02,F04)'),
(71, 6, '8-series (E31)'),
(72, 6, 'GT (F07)'),
(73, 6, 'M3 (E46)'),
(74, 6, 'M3 (E92)'),
(75, 6, 'M5 (E39)'),
(76, 6, 'M5 (E60)'),
(77, 6, 'M6'),
(78, 6, 'X1 (E84)'),
(79, 6, 'X3 (E83)'),
(80, 6, 'X3 (F25)'),
(81, 6, 'X5 (E53)'),
(82, 6, 'X5 (E70)'),
(83, 6, 'X5 M'),
(84, 6, 'X6 (E71)'),
(85, 6, 'X6 M'),
(86, 6, 'Z3'),
(87, 6, 'Z4 (E85)'),
(88, 6, 'Z4 (E89)'),
(89, 6, 'Z8'),
(90, 7, 'Century'),
(91, 7, 'Enclave'),
(92, 7, 'La Crosse'),
(93, 7, 'Le Sabre'),
(94, 7, 'Lucerne'),
(95, 7, 'Park Avenue'),
(96, 7, 'Rainier'),
(97, 7, 'Regal'),
(98, 7, 'Rendezvouz'),
(99, 7, 'Terraza'),
(100, 8, 'BLS'),
(101, 8, 'CTS'),
(102, 8, 'De Ville'),
(103, 8, 'DTS'),
(104, 8, 'Eldorado'),
(105, 8, 'Escalade'),
(106, 8, 'Seville'),
(107, 8, 'SRX'),
(108, 8, 'STS'),
(109, 8, 'XLR'),
(110, 9, 'Amulet'),
(111, 9, 'CrossEastar'),
(112, 9, 'Eastar'),
(113, 9, 'Fora'),
(114, 9, 'Kimo'),
(115, 9, 'M11'),
(116, 9, 'QQ'),
(117, 9, 'QQ6'),
(118, 9, 'Tiggo'),
(119, 10, 'Astro'),
(120, 10, 'Avalanche'),
(121, 10, 'Aveo'),
(122, 10, 'Blazer'),
(123, 10, 'Camaro'),
(124, 10, 'Captiva'),
(125, 10, 'Cavalier'),
(126, 10, 'Cobalt'),
(127, 10, 'Colorado'),
(128, 10, 'Corvette'),
(129, 10, 'Cruze'),
(130, 10, 'Epica'),
(131, 10, 'Equinox'),
(132, 10, 'Express'),
(133, 10, 'HHR'),
(134, 10, 'Impala'),
(135, 10, 'Lacetti'),
(136, 10, 'Lanos'),
(137, 10, 'Lumina'),
(138, 10, 'Malibu'),
(139, 10, 'Monte Carlo'),
(140, 10, 'Niva'),
(141, 10, 'Orlando'),
(142, 10, 'Rezzo'),
(143, 10, 'Silverado'),
(144, 10, 'Spark'),
(145, 10, 'SSR'),
(146, 10, 'Suburban'),
(147, 10, 'Tahoe'),
(148, 10, 'Town Country'),
(149, 10, 'TrailBlazer'),
(150, 10, 'Traverse'),
(151, 10, 'Uplander'),
(152, 10, 'Venture'),
(153, 11, '300C'),
(154, 11, '300M'),
(155, 11, 'Aspen'),
(156, 11, 'Concorde'),
(157, 11, 'Crossfire'),
(158, 11, 'Grand Voyager'),
(159, 11, 'Pacifica'),
(160, 11, 'PT Cruiser'),
(161, 11, 'Sebring'),
(162, 11, 'Town & Country'),
(163, 11, 'Voyager'),
(164, 12, 'Berlingo'),
(165, 12, 'C-Crosser'),
(166, 12, 'C1'),
(167, 12, 'C2'),
(168, 12, 'C3'),
(169, 12, 'C3 Picasso'),
(170, 12, 'C3 Pluriel'),
(171, 12, 'C4'),
(172, 12, 'C4 Picasso'),
(173, 12, 'C5'),
(174, 12, 'C6'),
(175, 12, 'C8'),
(176, 12, 'DS3'),
(177, 12, 'Grand C4 Picasso'),
(178, 12, 'Nemo'),
(179, 12, 'Saxo'),
(180, 12, 'Xsara'),
(181, 12, 'Xsara Picasso'),
(182, 13, 'Evanda'),
(183, 13, 'Kalos'),
(184, 13, 'Lacetti'),
(185, 13, 'Lanos'),
(186, 13, 'Leganza'),
(187, 13, 'Magnus'),
(188, 13, 'Matiz'),
(189, 13, 'Nexia'),
(190, 13, 'Nubira'),
(191, 13, 'Rezzo'),
(192, 14, 'Applause'),
(193, 14, 'Copen'),
(194, 14, 'Cuore'),
(195, 14, 'Gran Move'),
(196, 14, 'Materia'),
(197, 14, 'Sirion'),
(198, 14, 'Terios'),
(199, 14, 'Trevis'),
(200, 14, 'YRV'),
(201, 15, 'Avenger'),
(202, 15, 'Caliber'),
(203, 15, 'Caliber SRT4'),
(204, 15, 'Caravan'),
(205, 15, 'Challenger'),
(206, 15, 'Charger'),
(207, 15, 'Dakota'),
(208, 15, 'Durango'),
(209, 15, 'Intrepid'),
(210, 15, 'Journey'),
(211, 15, 'Magnum'),
(212, 15, 'Neon'),
(213, 15, 'Nitro'),
(214, 15, 'Ram 1500'),
(215, 15, 'Ram 2500'),
(216, 15, 'Ram SRT10'),
(217, 15, 'Stratus'),
(218, 15, 'Viper'),
(219, 16, 'Vita'),
(220, 17, '348 GT'),
(221, 17, '348 Spider'),
(222, 17, '355 F1 Berlinetta'),
(223, 17, '355 F1 GTS'),
(224, 17, '355 F1 Spider'),
(225, 17, '360 Modena'),
(226, 17, '360 Spider'),
(227, 17, '456 GT'),
(228, 17, '456 GTA'),
(229, 17, '456 M GT'),
(230, 17, '456 M GTA'),
(231, 17, '458 Italia'),
(232, 17, '512 TR'),
(233, 17, '550 Barchetta Pininfarina'),
(234, 17, '550 Maranello'),
(235, 17, '575 M Maranello'),
(236, 17, '599 GTB Fiorano'),
(237, 17, '612 Scaglietti'),
(238, 17, 'California'),
(239, 17, 'Challenge Stradale'),
(240, 17, 'Enzo'),
(241, 17, 'F355 Berlinetta'),
(242, 17, 'F355 GTS'),
(243, 17, 'F355 Spider'),
(244, 17, 'F430'),
(245, 17, 'F430 Challenge'),
(246, 17, 'F430 Spider'),
(247, 17, 'F50'),
(248, 17, 'F512 M'),
(249, 17, 'FXX'),
(250, 17, 'Superamerica'),
(251, 19, 'C-Max'),
(252, 19, 'Cougar'),
(253, 19, 'Crown Victoria'),
(254, 19, 'Edge'),
(255, 19, 'Escape'),
(256, 19, 'Excursion'),
(257, 19, 'Expedition'),
(258, 19, 'Explorer'),
(259, 19, 'F150'),
(260, 19, 'Fiesta'),
(261, 19, 'Five Hundred'),
(262, 19, 'Flex'),
(263, 19, 'Focus'),
(264, 19, 'Freestar'),
(265, 19, 'Freestyle'),
(266, 19, 'Fusion'),
(267, 19, 'Fusion USA'),
(268, 19, 'Galaxy'),
(269, 19, 'Grand C-MAX'),
(270, 19, 'GT'),
(271, 19, 'Ka'),
(272, 19, 'Kuga'),
(273, 19, 'Maverick'),
(274, 19, 'Mondeo'),
(275, 19, 'Mustang'),
(276, 19, 'Puma'),
(277, 19, 'Ranger'),
(278, 19, 'S-Max'),
(279, 19, 'Sport Trac'),
(280, 19, 'Taurus SE/SEL'),
(281, 19, 'Taurus X'),
(282, 19, 'Thunderbird'),
(283, 19, 'Tourneo Connect'),
(284, 19, 'Transit'),
(285, 19, 'Transit Connect'),
(286, 20, 'MK'),
(287, 20, 'Otaka'),
(288, 20, 'Vision'),
(289, 21, 'Acadia'),
(290, 21, 'Canyon'),
(291, 21, 'Envoy'),
(292, 21, 'Sierra 1500'),
(293, 21, 'Sierra 2500'),
(294, 21, 'Yukon'),
(295, 22, 'Cowry'),
(296, 22, 'Deer'),
(297, 22, 'GWPeri'),
(298, 22, 'Hover'),
(299, 22, 'Hover H3'),
(300, 22, 'Hover H5'),
(301, 22, 'Hover M2'),
(302, 22, 'Pegasus'),
(303, 22, 'Safe'),
(304, 22, 'Sailor'),
(305, 22, 'Sing'),
(306, 22, 'Socool'),
(307, 22, 'Wingle'),
(308, 22, 'Wingle 3'),
(309, 22, 'Wingle 5'),
(310, 23, 'Haima 3'),
(311, 24, 'Accord'),
(312, 24, 'Civic'),
(313, 24, 'CR-V'),
(314, 24, 'Crosstour'),
(315, 24, 'Element'),
(316, 24, 'Fit'),
(317, 24, 'FR-V'),
(318, 24, 'HR-V'),
(319, 24, 'Insight'),
(320, 24, 'Integra'),
(321, 24, 'Jazz'),
(322, 24, 'Legend'),
(323, 24, 'Odyssey'),
(324, 24, 'Pilot'),
(325, 24, 'Prelude'),
(326, 24, 'Ridgeline'),
(327, 24, 'S2000'),
(328, 24, 'Shuttle'),
(329, 24, 'Stream'),
(330, 25, 'H2'),
(331, 25, 'H3'),
(332, 26, 'Accent'),
(333, 26, 'Atos Prime'),
(334, 26, 'Azera'),
(335, 26, 'Centennial'),
(336, 26, 'Coupe'),
(337, 26, 'Elantra'),
(338, 26, 'Entourage'),
(339, 26, 'Equus'),
(340, 26, 'Galloper'),
(341, 26, 'Genesis'),
(342, 26, 'Genesis Coupe'),
(343, 26, 'Getz'),
(344, 26, 'Grandeur'),
(345, 26, 'H1'),
(346, 26, 'i10'),
(347, 26, 'i20'),
(348, 26, 'i30'),
(349, 26, 'ix35'),
(350, 26, 'ix55 (Veracruz)'),
(351, 26, 'Matrix'),
(352, 26, 'Porter'),
(353, 26, 'Porter II'),
(354, 26, 'Santa Fe'),
(355, 26, 'Solaris'),
(356, 26, 'Sonata'),
(357, 26, 'Sonata NF'),
(358, 26, 'Terracan'),
(359, 26, 'Trajet'),
(360, 26, 'Tucson'),
(361, 26, 'Verna'),
(362, 26, 'XG'),
(363, 27, 'EX35'),
(364, 27, 'EX37'),
(365, 27, 'FX35'),
(366, 27, 'FX45'),
(367, 27, 'FX50'),
(368, 27, 'G25'),
(369, 27, 'G35 Coupe'),
(370, 27, 'G35 Sedan'),
(371, 27, 'G37 Coupe'),
(372, 27, 'G37 Sedan'),
(373, 27, 'I35'),
(374, 27, 'M35'),
(375, 27, 'M45'),
(376, 27, 'Q45'),
(377, 27, 'QX4'),
(378, 27, 'QX56'),
(379, 28, 'Ascender'),
(380, 28, 'Axiom'),
(381, 28, 'D-Max Rodeo'),
(382, 28, 'I280'),
(383, 28, 'I290'),
(384, 28, 'I350'),
(385, 28, 'I370'),
(386, 28, 'Rodeo'),
(387, 28, 'Trooper'),
(388, 28, 'VehiCross'),
(389, 29, 'Daily Van'),
(390, 30, 'S-Type'),
(391, 30, 'X-Type'),
(392, 30, 'XF'),
(393, 30, 'XJ'),
(394, 30, 'XK'),
(395, 31, 'Cherokee'),
(396, 31, 'Commander'),
(397, 31, 'Compass'),
(398, 31, 'Grand Cherokee'),
(399, 31, 'Liberty'),
(400, 31, 'Wrangler'),
(401, 32, 'Carens'),
(402, 32, 'Carnival'),
(403, 32, 'Cee`d'),
(404, 32, 'Cerato'),
(405, 32, 'Clarus'),
(406, 32, 'Magentis'),
(407, 32, 'Mohave'),
(408, 32, 'Opirus'),
(409, 32, 'Optima'),
(410, 32, 'Picanto'),
(411, 32, 'Rio'),
(412, 32, 'Shuma'),
(413, 32, 'Sorento'),
(414, 32, 'Sorento New'),
(415, 32, 'Soul'),
(416, 32, 'Spectra'),
(417, 32, 'Sportage'),
(418, 32, 'Venga'),
(419, 33, 'Delta'),
(420, 33, 'Lybra'),
(421, 33, 'Musa'),
(422, 33, 'Phedra'),
(423, 33, 'Thesis'),
(424, 33, 'Ypsilon'),
(425, 34, 'Defender'),
(426, 34, 'Discovery 2'),
(427, 34, 'Discovery 3'),
(428, 34, 'Discovery 4'),
(429, 34, 'Evoque'),
(430, 34, 'Freelander'),
(431, 34, 'Freelander 2'),
(432, 34, 'Range Rover'),
(433, 34, 'Range Rover Sport'),
(434, 35, 'CT200h'),
(435, 35, 'ES300'),
(436, 35, 'ES330'),
(437, 35, 'ES350'),
(438, 35, 'GS300'),
(439, 35, 'GS350'),
(440, 35, 'GS400'),
(441, 35, 'GS430'),
(442, 35, 'GS450h'),
(443, 35, 'GS460'),
(444, 35, 'GX460'),
(445, 35, 'GX470'),
(446, 35, 'IS-F'),
(447, 35, 'IS200'),
(448, 35, 'IS250'),
(449, 35, 'IS300'),
(450, 35, 'IS350'),
(451, 35, 'LFA'),
(452, 35, 'LS400'),
(453, 35, 'LS430'),
(454, 35, 'LS460'),
(455, 35, 'LS600h'),
(456, 35, 'LX470'),
(457, 35, 'LX570'),
(458, 35, 'RX270'),
(459, 35, 'RX300'),
(460, 35, 'RX350'),
(461, 35, 'RX400h'),
(462, 35, 'RX450h'),
(463, 35, 'SC430'),
(464, 36, 'Breez'),
(465, 36, 'Smily'),
(466, 36, 'Solano'),
(467, 37, 'Aviator'),
(468, 37, 'LS'),
(469, 37, 'Mark LT'),
(470, 37, 'MKS'),
(471, 37, 'MKT'),
(472, 37, 'MKX'),
(473, 37, 'MKZ'),
(474, 37, 'Navigator'),
(475, 37, 'Town Car'),
(476, 37, 'Zephyr'),
(477, 38, 'Elise'),
(478, 38, 'Europa S'),
(479, 38, 'Exige'),
(480, 39, '3200 GT'),
(481, 39, 'Coupe'),
(482, 39, 'Gran Turismo'),
(483, 39, 'Gran Turismo S'),
(484, 39, 'Quattroporte'),
(485, 39, 'Quattroporte S'),
(486, 40, '57'),
(487, 40, '57 S'),
(488, 40, '62'),
(489, 40, '62 S'),
(490, 40, 'Landaulet'),
(491, 41, '2'),
(492, 41, '3'),
(493, 41, '323'),
(494, 41, '5'),
(495, 41, '6'),
(496, 41, '626'),
(497, 41, 'B-Series'),
(498, 41, 'BT-50'),
(499, 41, 'CX-5'),
(500, 41, 'CX-7'),
(501, 41, 'CX-9'),
(502, 41, 'MPV'),
(503, 41, 'MX-5 Miata'),
(504, 41, 'Premacy'),
(505, 41, 'RX-8'),
(506, 41, 'Tribute'),
(507, 42, 'A-Klasse (W168)'),
(508, 42, 'A-Klasse (W169)'),
(509, 42, 'B-Klasse (W245)'),
(510, 42, 'C-Klasse (CL203)'),
(511, 42, 'C-Klasse (W202)'),
(512, 42, 'C-Klasse (W203)'),
(513, 42, 'C-Klasse (W204)'),
(514, 42, 'CL-Klasse (C140)'),
(515, 42, 'CL-Klasse (C215)'),
(516, 42, 'CL-Klasse (C216)'),
(517, 42, 'CLC-Klasse'),
(518, 42, 'CLK-Klasse (W208)'),
(519, 42, 'CLK-Klasse (W209)'),
(520, 42, 'CLS-Klasse (C219)'),
(521, 42, 'E-Klasse (W210)'),
(522, 42, 'E-Klasse (W211)'),
(523, 42, 'E-Klasse (W212)'),
(524, 42, 'G-Klasse (W463)'),
(525, 42, 'GL-Klasse (X164)'),
(526, 42, 'GLK-Klasse (X204)'),
(527, 42, 'M-Klasse (W163)'),
(528, 42, 'M-Klasse (W164)'),
(529, 42, 'R-Klasse (W251)'),
(530, 42, 'S-Klasse (W140)'),
(531, 42, 'S-Klasse (W220)'),
(532, 42, 'S-Klasse (W221)'),
(533, 42, 'SL-Klasse (R230)'),
(534, 42, 'SLK-Klasse (R170)'),
(535, 42, 'SLK-Klasse (R171)'),
(536, 42, 'SLR-Klasse'),
(537, 42, 'Sprinter'),
(538, 42, 'Vaneo'),
(539, 42, 'Viano'),
(540, 42, 'Vito'),
(541, 43, 'Grand Marquis'),
(542, 43, 'Mariner'),
(543, 43, 'Milan'),
(544, 43, 'Montego'),
(545, 43, 'Monterey'),
(546, 43, 'Mountaineer'),
(547, 43, 'Sable'),
(548, 44, 'TF'),
(549, 44, 'XPower SV'),
(550, 44, 'ZR'),
(551, 44, 'ZS'),
(552, 44, 'ZT'),
(553, 44, 'ZT-T'),
(554, 45, 'Clubman'),
(555, 45, 'Clubman S'),
(556, 45, 'Cooper'),
(557, 45, 'Cooper Cabrio'),
(558, 45, 'Cooper Countryman'),
(559, 45, 'Cooper S'),
(560, 45, 'Cooper S All4 Countryman'),
(561, 45, 'Cooper S Cabrio'),
(562, 45, 'One'),
(563, 46, '3000 GT'),
(564, 46, 'ASX'),
(565, 46, 'Carisma'),
(566, 46, 'Colt'),
(567, 46, 'Eclipse'),
(568, 46, 'Endeavor'),
(569, 46, 'Galant'),
(570, 46, 'Grandis'),
(571, 46, 'L200'),
(572, 46, 'Lancer'),
(573, 46, 'Lancer Evo IX'),
(574, 46, 'Lancer Evo VII'),
(575, 46, 'Lancer Evo VIII'),
(576, 46, 'Lancer Evo X'),
(577, 46, 'Outlander'),
(578, 46, 'Outlander XL'),
(579, 46, 'Pajero'),
(580, 46, 'Pajero Pinin'),
(581, 46, 'Pajero Sport'),
(582, 46, 'Raider'),
(583, 46, 'Space Gear'),
(584, 46, 'Space Runner'),
(585, 46, 'Space Star'),
(586, 47, 'Almera'),
(587, 47, 'Almera Classic'),
(588, 47, 'Almera Tino'),
(589, 47, 'Altima'),
(590, 47, 'Armada'),
(591, 47, 'GT-R'),
(592, 47, 'Juke'),
(593, 47, 'Maxima'),
(594, 47, 'Micra'),
(595, 47, 'Murano'),
(596, 47, 'Navara'),
(597, 47, 'Note'),
(598, 47, 'NP300 Pick Up'),
(599, 47, 'Pathfinder'),
(600, 47, 'Patrol'),
(601, 47, 'Primera'),
(602, 47, 'Qashqai'),
(603, 47, 'Qashqai+2'),
(604, 47, 'Quest'),
(605, 47, 'Sentra'),
(606, 47, 'Skyline'),
(607, 47, 'Teana'),
(608, 47, 'Terrano 2'),
(609, 47, 'Tiida'),
(610, 47, 'X-Terra'),
(611, 47, 'X-Trail'),
(612, 47, 'Z'),
(613, 48, 'Agila'),
(614, 48, 'Antara'),
(615, 48, 'Astra G'),
(616, 48, 'Astra H'),
(617, 48, 'Astra J'),
(618, 48, 'Combo Tour'),
(619, 48, 'Corsa'),
(620, 48, 'Frontera'),
(621, 48, 'Insignia'),
(622, 48, 'Meriva'),
(623, 48, 'Monterey'),
(624, 48, 'Omega'),
(625, 48, 'Signum'),
(626, 48, 'Speedster'),
(627, 48, 'Tigra'),
(628, 48, 'Vectra B'),
(629, 48, 'Vectra C'),
(630, 48, 'Vivaro'),
(631, 48, 'Zafira'),
(632, 49, '1007'),
(633, 49, '107'),
(634, 49, '206'),
(635, 49, '207'),
(636, 49, '3008'),
(637, 49, '307'),
(638, 49, '308'),
(639, 49, '4007'),
(640, 49, '406'),
(641, 49, '407'),
(642, 49, '508'),
(643, 49, '607'),
(644, 49, '807'),
(645, 49, 'Boxer'),
(646, 49, 'Partner'),
(647, 49, 'Partner Origin VU'),
(648, 49, 'Partner Tepee'),
(649, 49, 'Partner VU'),
(650, 49, 'RCZ Sport'),
(651, 50, 'Aztec'),
(652, 50, 'Bonneville'),
(653, 50, 'Firebird'),
(654, 50, 'G5'),
(655, 50, 'G6'),
(656, 50, 'G8'),
(657, 50, 'Grand AM'),
(658, 50, 'Grand Prix'),
(659, 50, 'GTO'),
(660, 50, 'Montana'),
(661, 50, 'Solstice'),
(662, 50, 'Sunfire'),
(663, 50, 'Torrent'),
(664, 50, 'Trans Sport'),
(665, 50, 'Vibe'),
(666, 51, '911'),
(667, 51, 'Boxster'),
(668, 51, 'Cayenne'),
(669, 51, 'Cayman'),
(670, 51, 'Panamera'),
(671, 52, 'Avantime'),
(672, 52, 'Clio'),
(673, 52, 'Duster'),
(674, 52, 'Espace'),
(675, 52, 'Fluence'),
(676, 52, 'Kangoo'),
(677, 52, 'Kangoo Compact'),
(678, 52, 'Koleos'),
(679, 52, 'Laguna'),
(680, 52, 'Latitude'),
(681, 52, 'Logan'),
(682, 52, 'Master'),
(683, 52, 'Megane'),
(684, 52, 'Modus'),
(685, 52, 'Sandero'),
(686, 52, 'Sandero Stepway'),
(687, 52, 'Scenic'),
(688, 52, 'Symbol'),
(689, 52, 'Trafic'),
(690, 52, 'Twingo'),
(691, 52, 'Vel Satis'),
(692, 53, 'Phantom'),
(693, 54, '25'),
(694, 54, '400'),
(695, 54, '45'),
(696, 54, '600'),
(697, 54, '75'),
(698, 54, 'Streetwise'),
(699, 55, '9-2x'),
(700, 55, '9-3'),
(701, 55, '9-4x'),
(702, 55, '9-5'),
(703, 55, '9-7x'),
(704, 56, 'Aura'),
(705, 56, 'Ion'),
(706, 56, 'LW'),
(707, 56, 'Outlook'),
(708, 56, 'SC'),
(709, 56, 'Sky'),
(710, 56, 'Vue'),
(711, 57, 'tC'),
(712, 57, 'xA'),
(713, 57, 'xB'),
(714, 57, 'xD'),
(715, 58, 'Alhambra'),
(716, 58, 'Altea'),
(717, 58, 'Altea Freetrack'),
(718, 58, 'Altea XL'),
(719, 58, 'Arosa'),
(720, 58, 'Cordoba'),
(721, 58, 'Ibiza'),
(722, 58, 'Leon'),
(723, 58, 'Toledo'),
(724, 59, 'Fabia'),
(725, 59, 'Felicia'),
(726, 59, 'Octavia'),
(727, 59, 'Octavia Scout'),
(728, 59, 'Octavia Tour'),
(729, 59, 'Praktik'),
(730, 59, 'Roomster'),
(731, 59, 'Superb'),
(732, 59, 'Yeti'),
(733, 60, 'Forfour'),
(734, 60, 'Fourtwo'),
(735, 60, 'Roadster'),
(736, 61, 'Actyon'),
(737, 61, 'Chairman'),
(738, 61, 'Korando'),
(739, 61, 'Kyron'),
(740, 61, 'Musso'),
(741, 61, 'Musso Sport'),
(742, 61, 'Rexton'),
(743, 61, 'Rexton II'),
(744, 61, 'Rodius'),
(745, 62, 'Baja'),
(746, 62, 'Forester'),
(747, 62, 'Impreza'),
(748, 62, 'Impreza XV'),
(749, 62, 'Justy'),
(750, 62, 'Legacy'),
(751, 62, 'Outback'),
(752, 62, 'Traviq'),
(753, 62, 'Tribeca'),
(754, 63, 'Alto'),
(755, 63, 'Baleno'),
(756, 63, 'Grand Vitara'),
(757, 63, 'Grand Vitara XL7'),
(758, 63, 'Ignis'),
(759, 63, 'Jimny'),
(760, 63, 'Kizashi'),
(761, 63, 'Liana'),
(762, 63, 'Splash'),
(763, 63, 'Swift'),
(764, 63, 'SX4'),
(765, 63, 'Wagon R+'),
(766, 64, '4Runner'),
(767, 64, 'Alphard'),
(768, 64, 'Auris'),
(769, 64, 'Avalon'),
(770, 64, 'Avensis'),
(771, 64, 'Avensis Verso'),
(772, 64, 'Aygo'),
(773, 64, 'Camry'),
(774, 64, 'Celica'),
(775, 64, 'Corolla'),
(776, 64, 'Corolla Verso'),
(777, 64, 'FJ Cruiser'),
(778, 64, 'Fortuner'),
(779, 64, 'Hiace'),
(780, 64, 'Highlander'),
(781, 64, 'Hilux'),
(782, 64, 'IQ'),
(783, 64, 'Land Cruiser 100 GX'),
(784, 64, 'Land Cruiser 100 VX'),
(785, 64, 'Land Cruiser 200'),
(786, 64, 'Land Cruiser 80'),
(787, 64, 'Land Cruiser 90'),
(788, 64, 'Land Cruiser Prado'),
(789, 64, 'Matrix'),
(790, 64, 'MR2'),
(791, 64, 'Picnic'),
(792, 64, 'Previa'),
(793, 64, 'Prius'),
(794, 64, 'RAV4'),
(795, 64, 'Sequoia'),
(796, 64, 'Sienna'),
(797, 64, 'Tacoma'),
(798, 64, 'Tundra'),
(799, 64, 'Venza'),
(800, 64, 'Verso'),
(801, 64, 'Yaris'),
(802, 64, 'Yaris Verso'),
(803, 65, 'Amarok'),
(804, 65, 'Bora'),
(805, 65, 'Caddy'),
(806, 65, 'Crafter'),
(807, 65, 'CrossGolf'),
(808, 65, 'CrossPolo'),
(809, 65, 'CrossTouran'),
(810, 65, 'Eos'),
(811, 65, 'Fox'),
(812, 65, 'Golf IV'),
(813, 65, 'Golf V'),
(814, 65, 'Golf V Plus'),
(815, 65, 'Golf VI'),
(816, 65, 'Jetta'),
(817, 65, 'Lupo'),
(818, 65, 'Multivan'),
(819, 65, 'New Beetle'),
(820, 65, 'Passat'),
(821, 65, 'Passat CC'),
(822, 65, 'Phaeton'),
(823, 65, 'Pointer'),
(824, 65, 'Polo'),
(825, 65, 'Polo Sedan'),
(826, 65, 'Scirocco'),
(827, 65, 'Sharan'),
(828, 65, 'Tiguan'),
(829, 65, 'Touareg'),
(830, 65, 'Touran'),
(831, 65, 'Transporter'),
(832, 66, 'C30'),
(833, 66, 'C70 Convertible'),
(834, 66, 'C70 Coupe'),
(835, 66, 'S40'),
(836, 66, 'S60'),
(837, 66, 'S70'),
(838, 66, 'S80'),
(839, 66, 'S90'),
(840, 66, 'V40'),
(841, 66, 'V50'),
(842, 66, 'V70'),
(843, 66, 'V90'),
(844, 66, 'XC60'),
(845, 66, 'XC70'),
(846, 66, 'XC90'),
(847, 67, 'Chance'),
(848, 68, '110'),
(849, 68, '4X4'),
(850, 68, 'Classics'),
(851, 68, 'Granta'),
(852, 68, 'Kalina'),
(853, 68, 'Priora'),
(854, 68, 'Samara'),
(855, 70, 'Tager'),
(856, 70, 'Vortex Estina'),
(857, 71, 'Hunter'),
(858, 71, 'Patriot'),
(859, 71, 'Pickup');

INSERT INTO karma.year_car (year_value) VALUES
(1980), (1981), (1982), (1983), (1984), (1985),
(1986), (1987), (1988), (1989), (1990), (1991),
(1992), (1993), (1994), (1995), (1996), (1997),
(1998), (1999), (2000), (2001), (2002), (2003),
(2004), (2005), (2006), (2007), (2008), (2009),
(2010), (2011), (2012), (2013), (2014), (2015),
(2016), (2017), (2018), (2019);
