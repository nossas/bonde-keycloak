<#macro emailLayout>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bonde</title>
    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            line-height: 1.6;
            color: #333333;
            background-color: #f5f5f5;
        }
        
        .email-container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
        }
        
        .email-wrapper {
            background-color: #f5f5f5;
            padding: 20px 0;
            min-height: 100vh;
        }
        
        .header {
            background-color: #ffffff;
            padding: 40px 20px;
            text-align: center;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .logo {
            max-width: 150px;
            height: auto;
            display: inline-block;
            margin: 0;
        }
        
        .content {
            padding: 40px 20px;
            background-color: #ffffff;
            text-align: center;
        }
        
        .content-inner {
            max-width: 100%;
        }
        
        .content h1 {
            font-size: 24px;
            font-weight: 600;
            color: #333333;
            margin-bottom: 20px;
        }
        
        .content p {
            font-size: 16px;
            line-height: 1.6;
            color: #666666;
            margin-bottom: 20px;
        }
        
        .cta-button {
            display: inline-block;
            padding: 14px 40px;
            margin: 20px 0;
            background-color: #FF6B35;
            color: #ffffff;
            text-decoration: none;
            border-radius: 4px;
            font-weight: 600;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }
        
        .cta-button:hover {
            background-color: #E55A2B;
        }
        
        .footer {
            background-color: #f5f5f5;
            padding: 30px 20px;
            text-align: center;
            border-top: 1px solid #e0e0e0;
            font-size: 12px;
            color: #999999;
        }
        
        .footer a {
            color: #FF6B35;
            text-decoration: none;
        }
        
        .footer a:hover {
            text-decoration: underline;
        }
        
        .divider {
            height: 1px;
            background-color: #e0e0e0;
            margin: 20px 0;
        }
        
        .security-note {
            background-color: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 15px;
            margin: 20px 0;
            border-radius: 4px;
            font-size: 14px;
            color: #856404;
            text-align: left;
        }
        
        @media (max-width: 600px) {
            .content {
                padding: 20px 15px;
            }
            
            .content h1 {
                font-size: 20px;
            }
            
            .cta-button {
                display: block;
                width: 100%;
                padding: 16px 20px;
            }
        }
    </style>
</head>
<body>
    <div class="email-wrapper">
        <div class="email-container">
            <div class="header">
                <img src="https://bonde-lps-assets.s3.sa-east-1.amazonaws.com/logo_bonde.png" alt="Bonde" class="logo">
            </div>
            
            <div class="content">
                <div class="content-inner">
                    <#nested>
                </div>
            </div>
            
            <div class="footer">
                <div class="divider"></div>
                <p>
                    Este é um e-mail automático da plataforma Bonde.<br>
                    <a href="https://bonde.org">Visite nosso site</a>
                </p>
                <p style="margin-top: 10px; color: #cccccc;">
                    © 2026 Bonde. Todos os direitos reservados.
                </p>
            </div>
        </div>
    </div>
</body>
</html>
</#macro>
