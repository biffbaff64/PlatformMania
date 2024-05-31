package com.richikin.platformania.ui;

import com.richikin.platformania.config.Settings;
import com.richikin.platformania.scenes.OptionsPage;

public final class DevPanelData
{
    public static DeveloperPanel.DMEntry[][] developerMenu =
            {
                    {
                            new DeveloperPanel.DMEntry( "Google Sign In", Settings._PLAY_SERVICES, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Achievements", Settings._ACHIEVEMENTS, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Challenges", Settings._CHALLENGES, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Button Outlines", Settings._BUTTON_BOXES, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Marker Tiles", Settings._SPAWNPOINTS, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Box2D Physics", Settings._BOX2D_PHYSICS, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Box2D Debug", Settings._BOX2D_DEBUG, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Use Ashley ECS", Settings._USING_ASHLEY_ECS, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Demo Scroll", Settings._SCROLL_DEMO, false ),
                            new DeveloperPanel.DMEntry( "Autoplay", Settings._AUTOPLAY, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Shader Program", Settings._SHADER_PROGRAM, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Cull Sprites", Settings._CULL_SPRITES, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "GLProfiler", Settings._GL_PROFILER, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Menu Screen", Settings._MENU_SCENE, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Disable Enemies", Settings._DISABLE_ENEMIES, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "Menu Page Heaps", Settings._MENU_HEAPS, false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
                    {
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                            new DeveloperPanel.DMEntry( "", "", false ),
                    },
            };

    public static void initialiseDeveloperMenu( OptionsPage optionsPage )
    {
        optionsPage.developerPanel.devMenu = new DeveloperPanel.DMEntry[ developerMenu.length ][ DeveloperPanel._TABLE_COLUMNS ];

        for ( int row = 0; row < developerMenu.length; row++ )
        {
            for ( int column = 0; DeveloperPanel.columnEnabled[ column ] && column < DeveloperPanel._TABLE_COLUMNS; column++ )
            {
                optionsPage.developerPanel.devMenu[ row ][ column ] = developerMenu[ row ][ column ];
            }
        }
    }
}
