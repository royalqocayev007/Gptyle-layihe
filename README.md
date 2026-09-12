# AI Video Studio

AI əsaslı modul video yaratma Android tətbiqi.

## İlk MVP

- Yeni layihə yaratmaq
- Video ideyasını daxil etmək
- Layihələri saxlamaq
- Script mərhələsi
- Scene mərhələsi
- Voice/TTS
- Subtitle
- Music/SFX
- Timeline
- MP4 export

## Planlaşdırılan arxitektura

- Project Manager
- AI Manager
- Script Engine
- Scene Engine
- Image/Asset Engine
- Voice Engine
- Subtitle Engine
- Music/SFX Engine
- Timeline Editor
- Export Engine
- Provider Registry

## AI provider sistemi

Tətbiq bir provayderə bağlı olmayacaq.

Planlaşdırılan interfeyslər:

- TextProvider
- ImageProvider
- VoiceProvider
- MusicProvider
- VideoProvider

İstifadəçi hər mərhələdə provider/model seçə biləcək.

## Rejimlər

AUTO:
İdeyadan MP4-ə avtomatik proses.

STEP-BY-STEP:
Hər mərhələdə istifadəçi yoxlayır, dəyişir və təsdiqləyir.

## Versiya sistemi

Regenerate əvvəlki nəticəni silməməlidir.

Hər yaradılmış asset üçün:

- provider
- model
- prompt
- parameters
- cost
- createdAt
- source inputs
- output URI

saxlanacaq.

## Məqsəd

Telefon üzərindən professional AI video istehsal studiyası yaratmaq.
